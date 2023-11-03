package com.fmartinier.killerparty.ui.challenges

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fmartinier.killerparty.databinding.FragmentChallengeBinding
import com.fmartinier.killerparty.model.Challenge
import com.fmartinier.killerparty.services.ChallengeService
import com.fmartinier.killerparty.utils.showDeleteConfirmationDialog

class ChallengeViewHolder(
    private val fragmentChallengeBinding: FragmentChallengeBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(fragmentChallengeBinding.root) {

    private val challengeService = ChallengeService(context)
    lateinit var onChallengeRemoved: ((Challenge) -> Unit)

    fun bindChallenge(challenge: Challenge) {
        fragmentChallengeBinding.description.text = challenge.description

        fragmentChallengeBinding.deleteIcon.setOnClickListener {
            showDeleteConfirmationDialog(
                context = context,
                itemToRemove = challenge,
                function = onChallengeRemoved
            )
        }

        fragmentChallengeBinding.switchEnable.isChecked = challenge.enable
        fragmentChallengeBinding.switchEnable.setOnCheckedChangeListener { _, isChecked ->
            challengeService.setEnableById(challenge.id, isChecked)
        }
    }

}