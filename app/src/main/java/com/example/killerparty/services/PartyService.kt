package com.example.killerparty.services

import android.content.Context
import com.example.killerparty.db.repository.ChallengeRepository
import com.example.killerparty.db.repository.PartyRepository
import com.example.killerparty.db.repository.PlayerRepository
import com.example.killerparty.model.Challenge
import com.example.killerparty.model.Party
import com.example.killerparty.model.Player
import com.example.killerparty.model.enums.PartyState
import kotlin.random.Random


class PartyService(context: Context) {
    private val partyRepository = PartyRepository(context)
    private val challengeRepository = ChallengeRepository(context)
    private val playerRepository = PlayerRepository(context)

    fun findOrCreate(): Party {
        return partyRepository.findOrCreate()
    }

    fun findAll(): List<Party> {
        return partyRepository.findAll()
    }

    fun beginParty(party: Party, players: List<Player>) {
        partyRepository.modifyStateById(party.id, PartyState.IN_PROGRESS)
        giveChallengeToPlayers(players)
        // Envoyer un sms à tous les joueurs
    }

    fun findPlayers(history: Party): List<Player> {
        return playerRepository.findAllFromParty(history)
    }

    /**
     * Give random challenge to each player, and insert them in DB
     */
    private fun giveChallengeToPlayers(players: List<Player>) {
        val availableChallenges: MutableList<Challenge> = mutableListOf()
        availableChallenges.addAll(challengeRepository.findAll())
        players.forEach {
            val randomIndex = Random.nextInt(availableChallenges.size)
            val randomChallenge = availableChallenges[randomIndex]
            availableChallenges.remove(randomChallenge)
            playerRepository.giveChallenge(it.id, randomChallenge.id)
        }
    }
}