package com.fmartinier.killerparty.ui.challenges

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fmartinier.killerparty.databinding.FragmentChallengeBinding
import com.fmartinier.killerparty.model.Challenge

class ChallengeViewAdapter(
    private val challenges: MutableList<Challenge>,
    private val context: Context,
) : RecyclerView.Adapter<ChallengeViewHolder>() {

    lateinit var onChallengeRemoved: ((Challenge) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentChallengeBinding.inflate(from, parent, false)
        return ChallengeViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bindChallenge(challenges[position])
        holder.onChallengeRemoved = {
            this.onChallengeRemoved.invoke(it)
        }
    }

    override fun getItemCount(): Int {
        return challenges.size
    }
}