package com.fmartinier.killerparty.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.annotation.StringRes
import com.fmartinier.killerparty.R


fun <T> showDeleteConfirmationDialog(context: Context, itemToRemove: T, function: (T) -> Unit) {
    showConfirmationDialog(
        context,
        R.string.delete_confirmation,
        R.string.delete_confirmed,
        R.string.delete_cancel,
        itemToRemove,
        function
    )
}

fun <T> showConfirmationDialog(
    context: Context,
    @StringRes
    message: Int,
    @StringRes
    positiveAction: Int,
    @StringRes
    negativeAction: Int,
    elt: T,
    function: (T) -> Unit,
) {
    showConfirmationDialog(context, message, positiveAction, negativeAction) {
        function(elt)
    }
}

fun <T> showConfirmationDialog(
    context: Context,
    message: String,
    positiveAction: String,
    negativeAction: String,
    elt: T,
    function: (T) -> Unit,
) {
    showConfirmationDialog(context, message, positiveAction, negativeAction) {
        function(elt)
    }
}

fun showConfirmationDialog(
    context: Context,
    @StringRes
    message: Int,
    @StringRes
    positiveAction: Int,
    @StringRes
    negativeAction: Int,
    function: () -> Unit
) {
    showConfirmationDialog(
        context,
        context.getString(message),
        context.getString(positiveAction),
        context.getString(negativeAction),
        function
    )
}

fun showConfirmationDialog(
    context: Context,
    message: String,
    positiveAction: String,
    negativeAction: String,
    function: () -> Unit
) {
    AlertDialog.Builder(context)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveAction) { _, _ ->
            function()
        }
        .setNegativeButton(negativeAction, null)
        .show()
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