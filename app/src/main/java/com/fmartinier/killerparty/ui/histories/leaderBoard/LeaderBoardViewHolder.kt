package com.fmartinier.killerparty.ui.histories.leaderBoard

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fmartinier.killerparty.databinding.LeaderBoardItemBinding
import com.fmartinier.killerparty.model.Player

class LeaderBoardViewHolder(
    private val binding: LeaderBoardItemBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindPlayer(player: Player, position: Int, size: Int) {
        binding.playerPosition.text = position.toString()
        binding.playerName.text = player.name
        binding.progressBar.setMax(size)
        binding.progressBar.setProgress(player.score)
        binding.progressBar.setSecondaryProgress(player.score)
        binding.progressBar.setProgressText(player.score.toString())
    }

}