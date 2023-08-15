package com.example.killerparty.services

import android.content.Context
import com.example.killerparty.db.repository.PlayerRepository
import com.example.killerparty.db.repository.PlayerToChallengeRepository
import com.example.killerparty.model.Party
import com.example.killerparty.model.Player

class PlayerService(
    val context: Context
) {
    private val playerRepository = PlayerRepository(context)
    private val playerToChallengeRepository = PlayerToChallengeRepository(context)
    private val challengeService = ChallengeService(context)
    private val smsService = SmsService(context)

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
        val newChallengeForKiller = challengeService.findFromPlayer(killer)
        val newTargetForKiller = findTargetOf(player)
        smsService.sendSMS(
            killer.phone,
            "Bravo, vous avez killé ${player.name} !\nVoici votre nouvelle cible : ${newTargetForKiller.name}\nNouveau défi : ${newChallengeForKiller.description}"
        )
    }

    private fun findTargetOf(player: Player): Player {
        return playerRepository.findTargetOf(player)
    }
}