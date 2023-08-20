package com.fmartinier.killerparty.services

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast


class SmsService(
    val context: Context
) {

    fun sendSMS(phoneNo: String, msg: String) {
        try {
            val smsManager = context.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(formatPhoneNumber(phoneNo), null, msg, null, null)
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
    private fun formatPhoneNumber(phoneNo: String): String {
        val stringWithNoSpace = phoneNo.filter { !it.isWhitespace() }
        return stringWithNoSpace.replace("+33", "0")
    }
}