package com.example.killerparty.ui.party

import android.annotation.SuppressLint
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.killerparty.R
import com.example.killerparty.databinding.FragmentPartyBinding
import com.example.killerparty.model.Party
import com.example.killerparty.model.Player
import com.example.killerparty.services.PartyService
import com.example.killerparty.services.PlayerService
import com.example.killerparty.ui.dialogs.AddPlayerDialogFragment
import com.example.killerparty.utils.PLAYER_DESCRIPTION
import com.example.killerparty.utils.PLAYER_NAME
import com.example.killerparty.utils.PLAYER_PHONE
import com.example.killerparty.utils.showConfirmationDialog

class PartyFragment : Fragment() {

    private lateinit var binding: FragmentPartyBinding

    private val players: MutableList<Player> = mutableListOf()
    private lateinit var party: Party
    private lateinit var partyService: PartyService
    private lateinit var playerService: PlayerService

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

        binding.addPlayerButton.setOnClickListener {
            val dialog = AddPlayerDialogFragment()
            requireActivity().supportFragmentManager.setFragmentResultListener(
                PLAYER_DESCRIPTION,
                viewLifecycleOwner
            ) { requestKey, bundle ->
                val phoneNumber = bundle.getString(PLAYER_PHONE)
                val isValidPhoneNumber = PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)
                if (requestKey == PLAYER_DESCRIPTION && !bundle.getString(PLAYER_NAME)
                        .isNullOrEmpty() && !phoneNumber.isNullOrEmpty() && isValidPhoneNumber
                ) {
                    playerService.insertPlayer(
                        name = bundle.getString(PLAYER_NAME) ?: "",
                        phone = bundle.getString(PLAYER_PHONE) ?: "",
                        party = party
                    )
                    fillAllPlayers()
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

    private fun fillAllPlayers() {
        players.clear()
        players.addAll(playerService.findAllPlayersFromParty(party))
    }
}