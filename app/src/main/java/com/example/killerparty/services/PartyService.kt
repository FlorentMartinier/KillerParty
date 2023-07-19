package com.example.killerparty.services

import android.content.Context
import com.example.killerparty.db.repository.PartyRepository
import com.example.killerparty.model.Party

class PartyService(context: Context) {
    private val partyRepository = PartyRepository(context)

    fun findOrCreateNotStartedParty(): Party {
        return partyRepository.findOrCreateNotStartedParty()
    }

    fun beginParty() {

    }
}