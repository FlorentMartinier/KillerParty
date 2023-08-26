package com.fmartinier.killerparty.ui.histories

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.databinding.FragmentHistoricBinding
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.model.enums.PlayerState
import com.fmartinier.killerparty.services.PartyService
import com.fmartinier.killerparty.services.PlayerService
import com.fmartinier.killerparty.ui.histories.players.HistoricPlayerViewAdapter
import java.time.ZoneOffset

class HistoriesViewHolder(
    private val fragmentHistoricBinding: FragmentHistoricBinding,
    private val context: Context,
    private val partyService: PartyService,
    private val playerService: PlayerService,
) : RecyclerView.ViewHolder(fragmentHistoricBinding.root) {

    @SuppressLint("NotifyDataSetChanged")
    fun bindHistory(party: Party) {
        val players = partyService.findPlayers(party)
        val resources = context.resources
        var winner = party.winner ?: resources.getString(R.string.no_winner_yet)
        fragmentHistoricBinding.date.text =
            resources.getString(R.string.party_date, party.date?.atZone(ZoneOffset.UTC)?.toLocalDate().toString())
        fragmentHistoricBinding.partyState.text =
            resources.getString(R.string.party_state, resources.getString(party.state.translate()))
        fragmentHistoricBinding.winner.text = resources.getString(R.string.winner, winner)
        fragmentHistoricBinding.playerList.apply {
            val adapter = HistoricPlayerViewAdapter(players, context)
            adapter.onPlayerKilled = {
                playerService.killPlayer(it)
                it.state = PlayerState.KILLED
                adapter.notifyDataSetChanged()
                val inLifePlayers = players.filter { player -> player.state == PlayerState.IN_LIFE }
                if (inLifePlayers.size == 1) {
                    winner = inLifePlayers[0].name
                    partyService.declareWinner(inLifePlayers[0], party)
                    party.winner = winner
                }
            }
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

}