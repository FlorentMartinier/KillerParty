package com.example.killerparty.utils

import android.app.AlertDialog
import android.content.Context
import com.example.killerparty.R

fun <T> showDeleteConfirmationDialog(context: Context, itemToRemove: T, function: (T) -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(R.string.delete_confirmation)
        .setCancelable(false)
        .setPositiveButton(R.string.yes) { _, _ ->
            function.invoke(itemToRemove)
        }
        .setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
    val alert = builder.create()
    alert.show()
}

fun <T> showConfirmationDialog(context: Context, confirmationMessage: String, function: (T) -> Unit, params: T) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(confirmationMessage)
        .setCancelable(false)
        .setPositiveButton(R.string.yes) { _, _ ->
            function.invoke(params)
        }
        .setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
    val alert = builder.create()
    alert.show()
}