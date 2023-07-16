package com.example.killerparty.ui.challenges

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.databinding.FragmentChallengeBinding
import com.example.killerparty.model.Challenge

class ChallengeViewAdapter(
    private val challenges: List<Challenge>,
) : RecyclerView.Adapter<ChallengeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = FragmentChallengeBinding.inflate(from, parent, false)
        return ChallengeViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bindChallenge(challenges[position])
    }

    override fun getItemCount(): Int {
        return challenges.size
    }
}