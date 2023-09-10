package com.fmartinier.killerparty.ui.histories.leaderBoard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fmartinier.killerparty.databinding.LeaderBoardItemBinding
import com.fmartinier.killerparty.model.Player

class LeaderBoardViewAdapter(
    private val players: List<Player>,
    private val context: Context,
) : RecyclerView.Adapter<LeaderBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = LeaderBoardItemBinding.inflate(from, parent, false)
        return LeaderBoardViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int) {
        val player = players[position]
        holder.bindPlayer(player, position + 1, players.size)
    }

    override fun getItemCount(): Int {
        return players.size
    }
}