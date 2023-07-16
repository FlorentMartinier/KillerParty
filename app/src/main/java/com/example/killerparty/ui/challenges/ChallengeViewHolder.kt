package com.example.killerparty.ui.challenges

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.CHALLENGE_DESCRIPTION
import com.example.killerparty.databinding.FragmentChallengeBinding
import com.example.killerparty.db.MyDatabaseHelper
import com.example.killerparty.model.Challenge

class ChallengeViewHolder(
    private val fragmentChallengeBinding: FragmentChallengeBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(fragmentChallengeBinding.root) {

    fun bindChallenge(challenge: Challenge) {
        val db = MyDatabaseHelper(context)
        fragmentChallengeBinding.description.text = challenge.description

        fragmentChallengeBinding.imageView.setOnClickListener {
            Toast.makeText(context, "Suppression du défi ${challenge.id}", Toast.LENGTH_SHORT).show()
            db.deleteChallengeById(challenge.id)
        }
    }
}