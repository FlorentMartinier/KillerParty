package com.example.killerparty.services

import android.content.Context
import com.example.killerparty.db.repository.PlayerRepository
import com.example.killerparty.model.Party
import com.example.killerparty.model.Player

class PlayerService(context: Context) {
    private val playerRepository = PlayerRepository(context)

    /**
     * Insert player in DB and create put it in the current party
     */
    fun insertPlayer(name: String, phone: String, party: Party) {
        playerRepository.insert(
            name = name,
            phone = phone,
            party = party,
        )
    }

    fun deletePlayerById(id: Int) {
        playerRepository.deleteById(id)
    }

    fun findAllPlayersFromParty(party: Party): List<Player> {
        return playerRepository.findAllFromParty(party)
    }
}