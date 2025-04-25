package com.fmartinier.killerparty.ui.party

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.client.KillerBackClient
import com.fmartinier.killerparty.databinding.FragmentPartyBinding
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.services.PartyService
import com.fmartinier.killerparty.services.PlayerService
import com.fmartinier.killerparty.services.SessionService
import com.fmartinier.killerparty.services.SmsService
import com.fmartinier.killerparty.ui.dialogs.AddPlayerDialogFragment
import com.fmartinier.killerparty.utils.PLAYER_DESCRIPTION
import com.fmartinier.killerparty.utils.PLAYER_NAME
import com.fmartinier.killerparty.utils.PLAYER_PHONE
import com.fmartinier.killerparty.utils.showConfirmationDialog
import com.onegravity.contactpicker.contact.Contact
import com.onegravity.contactpicker.contact.ContactDescription
import com.onegravity.contactpicker.contact.ContactSortOrder
import com.onegravity.contactpicker.core.ContactPickerActivity
import com.onegravity.contactpicker.picture.ContactPictureType
import org.koin.android.ext.android.inject


class PartyFragment : Fragment() {

    private val killerBackClient: KillerBackClient by inject()
    private lateinit var binding: FragmentPartyBinding

    private val players: MutableList<Player> = mutableListOf()
    private lateinit var party: Party
    private lateinit var partyService: PartyService
    private lateinit var playerService: PlayerService
    private lateinit var smsService: SmsService
    private lateinit var sessionService: SessionService

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPartyBinding.inflate(inflater, container, false)
        binding.noPartyHelperMessage.editText.text = context?.getString(R.string.no_player_in_party)

        val requiredContext = requireContext()
        partyService = PartyService(requiredContext)
        playerService = PlayerService(requiredContext)
        smsService = SmsService(requiredContext)
        sessionService = SessionService(requiredContext, killerBackClient)
        party = partyService.findOrCreate()
        fillAllPlayers()

        binding.players.apply {
            layoutManager = LinearLayoutManager(context)
            val adapter = PlayerViewAdapter(players, context)
            adapter.onPlayerRemoved = {
                playerService.deleteById(it.id)
                players.remove(it)
                adapter.notifyDataSetChanged()
                manageHelperVisibility()
            }
            this.adapter = adapter
        }

        // Ajouter un joueur manuellement (ouvrir une fenêtre modale pour écrire les infos du joueur)
        /*
        binding.addPlayerButton.setOnClickListener {
            launchAddingPlayerModal()
        }
         */

        // Importer les infos d'un contact depuis le répertoire
        binding.importPlayerButton.setOnClickListener {
            pickContact()
        }

        // Créer ou copier un lien de session pour rejoindre la partie.
        binding.createSessionButton.setOnClickListener {
            sessionService.copySessionLinkToClipboard(requireContext())
        }

        binding.beginPartyButton.setOnClickListener {
            if (partyService.canBeginParty(requiredContext, party)) {
                showConfirmationDialog(
                    context = requiredContext,
                    message = getString(
                        R.string.begin_party_message,
                        players.size.toString()
                    ),
                    positiveAction = getString(R.string.begin_party_confirm),
                    negativeAction = getString(R.string.begin_party_cancel),
                    function = {
                        partyService.beginParty(party = party, players = players)
                        Toast.makeText(
                            context,
                            resources.getString(R.string.party_beginning),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }

        return binding.root
    }

    private fun launchAddingPlayerModal(playerName: String = "", playerPhone: String = "") {
        val dialog = AddPlayerDialogFragment(playerName = playerName, playerPhone = playerPhone)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            PLAYER_DESCRIPTION,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val phoneNumber = bundle.getString(PLAYER_PHONE)
            val namePlayer = bundle.getString(PLAYER_NAME)
            try {
                if (phoneNumber.isNullOrEmpty()) {
                    throw Exception(resources.getString(R.string.empty_phone_error))
                }
                if (namePlayer.isNullOrEmpty()) {
                    throw Exception(resources.getString(R.string.empty_player_error))
                }
                if (!smsService.isValidPhoneNumber(phoneNumber)) {
                    throw Exception(resources.getString(R.string.phone_number_format_error))
                }
                if (requestKey != PLAYER_DESCRIPTION) {
                    throw Exception(resources.getString(R.string.unknown_error))
                }
                insertPlayer(
                    name = namePlayer,
                    phone = phoneNumber
                )
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        dialog.show(activity?.supportFragmentManager!!, "")
    }

    private fun insertPlayer(name: String, phone: String) {
        playerService.insert(
            name = name,
            phone = phone,
            party = party
        )
        fillAllPlayers()
        binding.players.adapter?.notifyItemInserted(players.size)
    }

    private fun pickContact() {
        val intent: Intent = Intent(context, ContactPickerActivity::class.java)
            .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name)
            .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
            .putExtra(
                ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                ContactDescription.ADDRESS.name
            )
            .putExtra(
                ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE_WORK
            )
            .putExtra(
                ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                ContactSortOrder.AUTOMATIC.name
            ).putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, true)

        contactPickLaucher.launch(intent)
    }

    private var contactPickLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!.hasExtra(
                    ContactPickerActivity.RESULT_CONTACT_DATA
                )
            ) {
                val contacts =
                    result.data!!.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA) as List<Contact>
                showConfirmationDialog(
                    context = requireContext(),
                    message = getString(
                        R.string.insert_player_message,
                        contacts.size.toString()
                    ),
                    positiveAction = getString(R.string.add),
                    negativeAction = getString(R.string.cancel),
                    function = {
                        contacts.forEach {
                            insertPlayer(
                                name = "${it.firstName} ${it.lastName}",
                                phone = it.mapPhone.values.first()
                            )
                        }
                    }
                )
            }

        }

    private fun fillAllPlayers() {
        this.players.clear()
        this.players.addAll(playerService.findAllFromParty(party))
        manageHelperVisibility()
    }

    private fun manageHelperVisibility() {
        if (players.isEmpty()) {
            binding.noPartyHelperMessage.editText.visibility = View.VISIBLE
            binding.noPartyHelperMessage.imageView.visibility = View.VISIBLE
        } else {
            binding.noPartyHelperMessage.editText.visibility = View.INVISIBLE
            binding.noPartyHelperMessage.imageView.visibility = View.INVISIBLE
        }
    }
}