package com.example.killerparty.ui.histories.players

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.databinding.FragmentHistoricPlayerBinding
import com.example.killerparty.model.Player
import com.example.killerparty.services.PlayerService

class HistoricPlayerViewHolder(
    private val binding: FragmentHistoricPlayerBinding,
    private val context: Context,
    private val playerService: PlayerService,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindPlayer(player: Player) {
        binding.playerName.text = player.name
        binding.playerState.text = player.state.name
        binding.buttonKill.setOnClickListener {
            playerService.killPlayer(player)
        }
    }

}