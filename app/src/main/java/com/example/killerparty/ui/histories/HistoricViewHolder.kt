package com.example.killerparty.ui.histories

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.databinding.FragmentHistoricBinding
import com.example.killerparty.model.Party
import com.example.killerparty.services.PartyService

class HistoricViewHolder(
    private val fragmentHistoricBinding: FragmentHistoricBinding,
    private val context: Context,
    private val partyService: PartyService,
) : RecyclerView.ViewHolder(fragmentHistoricBinding.root) {

    fun bindHistory(party: Party) {
        // TODO : lister tous les joueurs en lice dans la party
        // Créer un bouton permettant de killer un joueur
        // quand un joueur est killé, un sms est envoyé au killer pour définir sa nouvelle cible.
        fragmentHistoricBinding.partyState.text = party.state.name
        fragmentHistoricBinding.winner.text = "test"
        val players = partyService.findPlayers(party)
        println("players : $players")
    }

}