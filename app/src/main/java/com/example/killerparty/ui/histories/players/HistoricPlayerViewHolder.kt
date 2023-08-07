package com.example.killerparty.ui.histories.players

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.databinding.FragmentHistoricPlayerBinding
import com.example.killerparty.model.Player
import com.example.killerparty.services.PlayerService

class HistoricPlayerViewHolder(
    private val fragmentHistoricBinding: FragmentHistoricPlayerBinding,
    private val context: Context,
    private val playerService: PlayerService,
) : RecyclerView.ViewHolder(fragmentHistoricBinding.root) {

    fun bindPlayer(player: Player) {
        fragmentHistoricBinding.playerName.text = player.name
        fragmentHistoricBinding.playerState.text = player.state.name
    }

}