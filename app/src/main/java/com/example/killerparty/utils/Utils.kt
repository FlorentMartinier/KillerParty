package com.example.killerparty.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.view.View
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

fun <T> showConfirmationDialog(
    context: Context,
    confirmationMessage: String,
    function: (T) -> Unit,
    params: T
) {
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

fun showConfirmationDialog(context: Context, confirmationMessage: String, function: () -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(confirmationMessage)
        .setCancelable(false)
        .setPositiveButton(R.string.yes) { _, _ ->
            function.invoke()
        }
        .setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
    val alert = builder.create()
    alert.show()
}

fun navigateTo(context: Context, resourceId: Int) {
    getActivity(context)?.findViewById<View>(resourceId)?.performClick()
}

fun getActivity(context: Context?): Activity? {
    if (context == null) {
        return null
    } else if (context is ContextWrapper) {
        return if (context is Activity) {
            context
        } else {
            getActivity(context.baseContext)
        }
    }
    return null
}