package com.example.killerparty.ui.challenges

import androidx.recyclerview.widget.RecyclerView
import com.example.killerparty.databinding.FragmentChallengeBinding
import com.example.killerparty.model.Challenge

class ChallengeViewHolder(
    private val fragmentChallengeBinding: FragmentChallengeBinding,
    //private val context: Context,
) : RecyclerView.ViewHolder(fragmentChallengeBinding.root) {

    lateinit var onChallengeRemoved: ((Challenge) -> Unit)

    fun bindChallenge(challenge: Challenge?) {
        fragmentChallengeBinding.description.text = challenge?.description

        fragmentChallengeBinding.deleteChallengeIcon.setOnClickListener {
            /*
            AlertDialog.Builder(context)
                .setTitle("Alerte")
                .setMessage("Etes vous sûr de supprimer ce défi ?")
                .setPositiveButton("Oui", DialogInterface.OnClickListener() {

                })
             */
            onChallengeRemoved.invoke(challenge!!)
        }
    }

}