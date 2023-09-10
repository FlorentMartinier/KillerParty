package com.fmartinier.killerparty.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.ui.histories.leaderBoard.LeaderBoardViewAdapter
import com.fmartinier.killerparty.utils.LEADER_BOARD


class LeaderBoardDialogFragment(
    val players: List<Player>,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val view =
                layoutInflater.inflate(R.layout.leader_board_modal, null)
            val leaderBoardView = view.findViewWithTag<RecyclerView>(LEADER_BOARD)
            leaderBoardView.apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = LeaderBoardViewAdapter(
                    players.sortedByDescending { player -> player.score },
                    context
                )
            }
            builder.setView(view).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}