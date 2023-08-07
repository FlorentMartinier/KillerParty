package com.example.killerparty.ui.histories.players

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.databinding.FragmentHistoricPlayerBinding
import com.example.killerparty.model.Player
import com.example.killerparty.services.PlayerService

class HistoricPlayerViewAdapter(
    private val players: List<Player>,
    private val context: Context,
    private val playerService: PlayerService,
) : RecyclerView.Adapter<HistoricPlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricPlayerViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentHistoricPlayerBinding.inflate(from, parent, false)
        return HistoricPlayerViewHolder(binding, context, playerService)
    }

    override fun onBindViewHolder(holder: HistoricPlayerViewHolder, position: Int) {
        holder.bindPlayer(players[position])
    }

    override fun getItemCount(): Int {
        return players.size
    }
}