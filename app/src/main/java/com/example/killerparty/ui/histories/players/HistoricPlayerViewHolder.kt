package com.example.killerparty.ui.histories.players

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.R
import com.example.killerparty.databinding.FragmentHistoricPlayerBinding
import com.example.killerparty.model.Challenge
import com.example.killerparty.model.Player
import com.example.killerparty.model.enums.PlayerState
import com.example.killerparty.services.PlayerService
import com.example.killerparty.utils.showConfirmationDialog
import com.example.killerparty.utils.showDeleteConfirmationDialog

class HistoricPlayerViewHolder(
    private val binding: FragmentHistoricPlayerBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var onPlayerKilled: ((Player) -> Unit)
    private val resources = context.resources

    fun bindPlayer(player: Player) {
        binding.playerName.text = player.name
        binding.playerState.text = resources.getString(player.state.translate())
        binding.buttonKill.isEnabled = player.state == PlayerState.IN_LIFE
        binding.buttonKill.setOnClickListener {
            showConfirmationDialog(
                context = context,
                confirmationMessage = context.resources.getString(
                    R.string.kill_confirmation,
                    player.name
                ),
                function = onPlayerKilled,
                params = player,
            )

        }
    }

}