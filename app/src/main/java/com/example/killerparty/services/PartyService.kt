package com.example.killerparty.services

import android.content.Context
import android.widget.Toast
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
        giveChallengeToPlayers(players)
        partyRepository.modifyStateById(party.id, PartyState.IN_PROGRESS)
        // TODO : Envoyer un sms à tous les joueurs
    }

    fun canBeginParty(context: Context, party: Party): Boolean {
        val players = findPlayers(party)
        val challenges = challengeRepository.findAll()
        return if(players.size < 2) {
            Toast.makeText(context, "Le nombre de joueur n'est pas suffisant !", Toast.LENGTH_SHORT).show()
            false
        } else if(challenges.size < players.size) {
            Toast.makeText(context, "Il y a plus de joueurs que de défis !", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
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