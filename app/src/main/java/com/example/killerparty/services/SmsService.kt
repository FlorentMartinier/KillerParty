package com.example.killerparty.services

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast


class SmsService(
    val context: Context
) {

    fun sendSMS(phoneNo: String, msg: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
            ex.printStackTrace()
        }
    }
}