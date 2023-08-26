package com.fmartinier.killerparty.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.widget.Toast


class SmsService(
    val context: Context
) {

    fun sendSMS(phoneNo: String, msg: String) {
        try {
            val formattedPhoneNo = formatPhoneNumber(phoneNo)
            val sentPI = PendingIntent.getBroadcast(
                context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE
            )
            getSmsManager().sendTextMessage(formattedPhoneNo, null, msg, sentPI, null)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
            ex.printStackTrace()
        }
    }

    fun isValidPhoneNumber(phoneNo: String): Boolean {
        val regexToMatch =
            Regex("^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$")
        return phoneNo.matches(regexToMatch)
    }

    /**
     * format phone number to be valid at sending
     * Ex :
     *  - +33 6 00 00 00 00 => 0600000000
     *  - 06 00 00 00 00 => 0600000000
     *  - 0600000000 => 0600000000
     */
    fun formatPhoneNumber(phoneNo: String): String {
        val stringWithNoSpace = phoneNo.filter { !it.isWhitespace() }
        return stringWithNoSpace.replace("+33", "0")
    }

    /**
     * Get SMS manager with backward compatibility for old phones
     */
    private fun getSmsManager(): SmsManager {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            // Use deprecated SmsManager to old sdk versions, for old phones
            SmsManager.getDefault()
        } else {
            context.getSystemService(SmsManager::class.java)
        }
    }
}