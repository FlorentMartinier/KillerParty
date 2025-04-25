package com.fmartinier.killerparty.services

import android.content.Context
import com.fmartinier.killerparty.client.KillerBackClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import com.fmartinier.killerparty.R

class SessionService(context: Context, val killerBackClient: KillerBackClient) {
    private val partyService = PartyService(context)

    fun copySessionLinkToClipboard(context: Context) {
        val party = partyService.findOrCreate()
        if(party.sessionId?.isNotEmpty() == true) {
            copyToClipboard(context, party.sessionId)
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            val response = withContext(Dispatchers.IO) {
                killerBackClient.createSession().execute()
            }.body()
            response?.get("id")?.let {
                partyService.modifySessionIdById(party.id, it.toString())
                copyToClipboard(context, it.toString())
            }
        }
    }

    private fun copyToClipboard(context: Context, text: String) {
        val baseurl = context.getString(R.string.killer_back_api_base_url)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("sessionId", "$baseurl#/$text")
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.shared_link_copied), Toast.LENGTH_SHORT).show()
    }
}