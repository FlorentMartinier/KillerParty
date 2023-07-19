package com.example.killerparty.ui.party

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.databinding.FragmentPlayerBinding
import com.example.killerparty.model.Player

class PlayerViewAdapter(
    private val players: MutableList<Player>,
    private val context: Context,
) : RecyclerView.Adapter<PlayerViewHolder>() {

    lateinit var onPlayerRemoved: ((Player) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentPlayerBinding.inflate(from, parent, false)
        return PlayerViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bindPlayer(players[position])
        holder.onPlayerRemoved = {
            this.onPlayerRemoved.invoke(it)
        }
    }

    override fun getItemCount(): Int {
        return players.size
    }
}