package com.example.killerparty.services

import android.content.Context
import android.widget.Toast
import com.example.killerparty.db.repository.PlayerRepository
import com.example.killerparty.db.repository.PlayerToChallengeRepository
import com.example.killerparty.model.Party
import com.example.killerparty.model.Player
import com.example.killerparty.model.enums.PlayerState

class PlayerService(
    val context: Context
) {
    private val playerRepository = PlayerRepository(context)
    private val playerToChallengeRepository = PlayerToChallengeRepository(context)

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

    fun killPlayer(player: Player) {
        playerRepository.kill(player)
        val killer = playerRepository.findKillerOf(player)
        playerToChallengeRepository.achieveChallengeWithTarget(player)
        playerToChallengeRepository.modifyChallengeKiller(player, killer)
        Toast.makeText(context, "joueur killé : ${player.name}; Killer : ${killer.name}", Toast.LENGTH_SHORT)
            .show()
    }
}