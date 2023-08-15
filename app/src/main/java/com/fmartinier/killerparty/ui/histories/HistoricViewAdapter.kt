package com.fmartinier.killerparty.ui.histories

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fmartinier.killerparty.databinding.FragmentHistoricBinding
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.services.PartyService
import com.fmartinier.killerparty.services.PlayerService

class HistoricViewAdapter(
    private val histories: MutableList<Party>,
    private val context: Context,
    private val partyService: PartyService,
    private val playerService: PlayerService,
) : RecyclerView.Adapter<HistoricViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentHistoricBinding.inflate(from, parent, false)
        return HistoricViewHolder(binding, context, partyService, playerService)
    }

    override fun onBindViewHolder(holder: HistoricViewHolder, position: Int) {
        holder.bindHistory(histories[position])
    }

    override fun getItemCount(): Int {
        return histories.size
    }
}