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
import com.fmartinier.killerparty.model.Party

class SessionService(context: Context, val killerBackClient: KillerBackClient) {
    private val partyService = PartyService(context)

    fun createSessionForParty(party: Party) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = withContext(Dispatchers.IO) {
                killerBackClient.createSession().execute()
            }.body()
            response?.get("id")?.let {
                partyService.modifySessionIdById(party.id, it.toString())
            }
        }
    }

    fun copySessionLinkToClipboard(context: Context, party: Party) {
        if(party.sessionId?.isNotEmpty() == true) {
            copyToClipboard(context, party.sessionId)
            return
        } else {
            Toast.makeText(context, "Session error", Toast.LENGTH_SHORT).show()
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