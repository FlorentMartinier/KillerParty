package com.example.killerparty.ui.histories.players

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.databinding.FragmentHistoricPlayerBinding
import com.example.killerparty.model.Player
import com.example.killerparty.model.enums.PlayerState
import com.example.killerparty.services.PlayerService

class HistoricPlayerViewAdapter(
    private val players: List<Player>,
    private val context: Context,
) : RecyclerView.Adapter<HistoricPlayerViewHolder>() {

    lateinit var onPlayerKilled: ((Player) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricPlayerViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentHistoricPlayerBinding.inflate(from, parent, false)
        return HistoricPlayerViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: HistoricPlayerViewHolder, position: Int) {
        val player = players[position]
        val isInLife = player.state == PlayerState.IN_LIFE
        val lastOneIneLife = players.filter { it.state == PlayerState.IN_LIFE }.size == 1
        holder.bindPlayer(player, isInLife && !lastOneIneLife)
        holder.onPlayerKilled = {
            this.onPlayerKilled.invoke(it)
        }
    }

    override fun getItemCount(): Int {
        return players.size
    }
}