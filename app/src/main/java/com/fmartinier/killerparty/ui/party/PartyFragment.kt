package com.fmartinier.killerparty.ui.party

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
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
import com.fmartinier.killerparty.databinding.FragmentPartyBinding
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.services.PartyService
import com.fmartinier.killerparty.services.PlayerService
import com.fmartinier.killerparty.services.SmsService
import com.fmartinier.killerparty.ui.dialogs.AddPlayerDialogFragment
import com.fmartinier.killerparty.utils.PLAYER_DESCRIPTION
import com.fmartinier.killerparty.utils.PLAYER_NAME
import com.fmartinier.killerparty.utils.PLAYER_PHONE
import com.fmartinier.killerparty.utils.showConfirmationDialog


class PartyFragment : Fragment() {

    private lateinit var binding: FragmentPartyBinding

    private val players: MutableList<Player> = mutableListOf()
    private lateinit var party: Party
    private lateinit var partyService: PartyService
    private lateinit var playerService: PlayerService
    private lateinit var smsService: SmsService

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPartyBinding.inflate(inflater, container, false)

        val requiredContext = requireContext()
        partyService = PartyService(requiredContext)
        playerService = PlayerService(requiredContext)
        smsService = SmsService(requiredContext)
        party = partyService.findOrCreate()
        fillAllPlayers()

        binding.players.apply {
            layoutManager = LinearLayoutManager(context)
            val adapter = PlayerViewAdapter(players, context)
            adapter.onPlayerRemoved = {
                playerService.deletePlayerById(it.id)
                players.remove(it)
                adapter.notifyDataSetChanged()
            }
            this.adapter = adapter
        }

        // Ajouter un joueur manuellement (ouvrir une fenêtre modale pour écrire les infos du joueur)
        binding.addPlayerButton.setOnClickListener {
            launchAddingPlayerModal()
        }

        // Importer les infos d'un contact depuis le répertoire
        binding.importPlayerButton.setOnClickListener {
            pickContact()
        }

        binding.beginPartyButton.setOnClickListener {
            if (partyService.canBeginParty(requiredContext, party)) {
                showConfirmationDialog(
                    context = requiredContext,
                    confirmationMessage = resources.getString(
                        R.string.begin_party_confirmation,
                        players.size.toString()
                    ),
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
            if (requestKey == PLAYER_DESCRIPTION && !bundle.getString(PLAYER_NAME)
                    .isNullOrEmpty() && !phoneNumber.isNullOrEmpty() && smsService.isValidPhoneNumber(phoneNumber)
            ) {
                playerService.insertPlayer(
                    name = bundle.getString(PLAYER_NAME) ?: "",
                    phone = bundle.getString(PLAYER_PHONE) ?: "",
                    party = party
                )
                fillAllPlayers()
                binding.players.adapter?.notifyItemInserted(players.size)
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.invalid_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        dialog.show(activity?.supportFragmentManager!!, "")
    }

    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        contactPickLaucher.launch(intent)
    }

    private var contactPickLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                // There are no request codes
                val data: Intent = result.data!!
                val contactUri: Uri = data.data ?: Uri.EMPTY
                val fieldsToQuery = arrayOf(
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER,
                )
                val cursor: Cursor? = requireContext().contentResolver?.query(
                    contactUri,
                    fieldsToQuery, null, null, null
                )
                cursor?.moveToFirst()
                val id = cursor?.getString(0) ?: ""
                val name = cursor?.getString(1) ?: ""
                val hasPhoneNumber = cursor?.getInt(2)
                val phoneNumbers = getContactPhoneNumbers(hasPhoneNumber == 1, id)
                cursor?.close()
                launchAddingPlayerModal(playerName = name, playerPhone = phoneNumbers[0])
            }
        }

    private fun getContactPhoneNumbers(hasPhoneNumber: Boolean, contactId: String): List<String> {
        val phoneNumbers = mutableListOf<String>()
        //check if contact has a phone number or not
        if (hasPhoneNumber) {
            val cursor = requireContext().contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                null,
                null
            )
            //a contact may have multiple phone numbers
            while (cursor!!.moveToNext()) {
                //get phone number
                val contactNumber = cursor.getString(0)
                phoneNumbers.add(contactNumber)
            }
            cursor.close()
        }
        return phoneNumbers
    }

    private fun fillAllPlayers() {
        this.players.clear()
        this.players.addAll(playerService.findAllPlayersFromParty(party))
    }
}