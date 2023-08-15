package com.fmartinier.killerparty.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.fmartinier.killerparty.utils.CHALLENGE_DESCRIPTION
import com.fmartinier.killerparty.R


class AddChallengeDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val view =
                layoutInflater.inflate(R.layout.add_challenge_modal, null)
            val bundle = Bundle()
            builder.setView(view)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    val editText = view.findViewWithTag<EditText>(CHALLENGE_DESCRIPTION)
                    bundle.putString(CHALLENGE_DESCRIPTION, editText.text.toString())
                    requireActivity().supportFragmentManager.setFragmentResult(CHALLENGE_DESCRIPTION, bundle)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    bundle.putString(CHALLENGE_DESCRIPTION, null)
                    requireActivity().supportFragmentManager.setFragmentResult(CHALLENGE_DESCRIPTION, bundle)
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}