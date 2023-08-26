package com.fmartinier.killerparty.ui.histories

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.fmartinier.killerparty.databinding.FragmentHistoricBinding
import com.fmartinier.killerparty.extensions.isNearOf
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.services.PartyService
import com.fmartinier.killerparty.services.PlayerService
import java.time.Instant

class HistoriesViewAdapter(
    private val histories: MutableList<Party>,
    private val context: Context,
    private val partyService: PartyService,
    private val playerService: PlayerService,
) : RecyclerView.Adapter<HistoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentHistoricBinding.inflate(from, parent, false)
        return HistoriesViewHolder(binding, context, partyService, playerService)
    }

    override fun onBindViewHolder(holder: HistoriesViewHolder, position: Int) {
        val history = histories[position]
        if (position == 0 && history.date?.isNearOf(Instant.now()) == true) {
            YoYo.with(Techniques.RubberBand)
                .duration(700)
                .playOn(holder.itemView)
        }
        holder.bindHistory(history)
    }

    override fun getItemCount(): Int {
        return histories.size
    }
}