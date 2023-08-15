package com.fmartinier.killerparty.ui.party

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fmartinier.killerparty.databinding.FragmentPlayerBinding
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.utils.showDeleteConfirmationDialog

class PlayerViewHolder(
    private val fragmentPlayerBinding: FragmentPlayerBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(fragmentPlayerBinding.root) {

    lateinit var onPlayerRemoved: ((Player) -> Unit)

    fun bindPlayer(player: Player) {
        fragmentPlayerBinding.playerName.text = player.name
        fragmentPlayerBinding.phone.text = player.phone

        fragmentPlayerBinding.deleteIcon.setOnClickListener {
            showDeleteConfirmationDialog(
                context = context,
                itemToRemove = player,
                function = onPlayerRemoved
            )
        }
    }

}