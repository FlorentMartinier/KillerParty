package com.fmartinier.killerparty.services

import android.content.Context
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.db.repository.PlayerRepository
import com.fmartinier.killerparty.db.repository.PlayerToChallengeRepository
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.model.Player

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
    fun insert(name: String, phone: String, party: Party) {
        playerRepository.insert(
            name = name,
            phone = smsService.formatPhoneNumber(phone),
            party = party,
        )
    }

    fun deleteById(id: Int) {
        playerRepository.deleteById(id)
    }

    fun findAllFromParty(party: Party): List<Player> {
        return playerRepository.findAllFromParty(party)
    }

    fun isThereAWinner(party: Party): Boolean {
        return playerRepository.isThereAWinner(party)
    }

    fun kill(player: Player, party: Party): Player {
        playerRepository.kill(player)
        val killer = playerRepository.findKillerOf(player)
        playerToChallengeRepository.achieveChallengeWithTarget(player)
        val scoreToAdd: Int

        if (isThereAWinner(party)) {
            // If he is the winner, +5 point to his score
            scoreToAdd = 5
            smsService.sendSMS(
                killer.phone,
                context.resources.getString(
                    R.string.sms_youre_the_winner,
                    killer.name,
                )
            )
        } else {
            scoreToAdd = 1
            playerToChallengeRepository.modifyChallengeKiller(player, killer)
            val newChallengeForKiller = challengeService.findActiveFromPlayer(killer)
            val newTargetForKiller = findTargetOf(killer)
            smsService.sendSMS(
                killer.phone,
                context.resources.getString(
                    R.string.sms_kill_player,
                    player.name,
                    newTargetForKiller.name,
                    newChallengeForKiller.description
                )
            )
        }
        playerRepository.addScore(killer, scoreToAdd)
        killer.score += scoreToAdd
        return killer
    }

    private fun findTargetOf(player: Player): Player {
        return playerRepository.findTargetOf(player)
    }
}