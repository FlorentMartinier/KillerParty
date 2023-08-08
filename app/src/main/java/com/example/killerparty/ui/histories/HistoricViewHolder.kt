package com.example.killerparty.ui.histories

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.R
import com.example.killerparty.databinding.FragmentHistoricBinding
import com.example.killerparty.model.Party
import com.example.killerparty.services.PartyService
import com.example.killerparty.services.PlayerService
import com.example.killerparty.ui.histories.players.HistoricPlayerViewAdapter

class HistoricViewHolder(
    private val fragmentHistoricBinding: FragmentHistoricBinding,
    private val context: Context,
    private val partyService: PartyService,
    private val playerService: PlayerService,
) : RecyclerView.ViewHolder(fragmentHistoricBinding.root) {

    fun bindHistory(party: Party) {
        // TODO : quand un joueur est killé, un sms est envoyé au killer pour définir sa nouvelle cible.
        val players = partyService.findPlayers(party)
        val resources = context.resources
        val winner = party.winner ?: resources.getString(R.string.no_winner_yet)
        fragmentHistoricBinding.date.text = resources.getString(R.string.party_date, party.date.toString())
        fragmentHistoricBinding.partyState.text = resources.getString(R.string.party_state, resources.getString(party.state.translate()))
        fragmentHistoricBinding.winner.text = resources.getString(R.string.winner, winner)
        fragmentHistoricBinding.playerList.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = HistoricPlayerViewAdapter(players, context, playerService)
        }
        println("players : $players")
    }

}