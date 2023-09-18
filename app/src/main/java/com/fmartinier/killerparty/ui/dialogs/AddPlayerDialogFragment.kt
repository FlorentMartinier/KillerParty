package com.fmartinier.killerparty.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.utils.PLAYER_DESCRIPTION
import com.fmartinier.killerparty.utils.PLAYER_NAME
import com.fmartinier.killerparty.utils.PLAYER_PHONE


class AddPlayerDialogFragment(
    private val playerName : String = "",
    private val playerPhone : String = "",
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val view =
                layoutInflater.inflate(R.layout.add_player_modal, null)
            val playerNameEditText = view.findViewWithTag<EditText>(PLAYER_NAME)
            val playerPhoneEditText = view.findViewWithTag<EditText>(PLAYER_PHONE)
            playerNameEditText.setText(playerName)
            playerPhoneEditText.setText(playerPhone)
            val bundle = Bundle()
            builder.setView(view)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    bundle.putString(PLAYER_NAME, playerNameEditText.text.toString())
                    bundle.putString(PLAYER_PHONE, playerPhoneEditText.text.toString())
                    requireActivity().supportFragmentManager.setFragmentResult(PLAYER_DESCRIPTION, bundle)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    bundle.putString(PLAYER_DESCRIPTION, null)
                    requireActivity().supportFragmentManager.setFragmentResult(PLAYER_DESCRIPTION, bundle)
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}