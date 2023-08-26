package com.fmartinier.killerparty.services

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.db.repository.ChallengeRepository
import com.fmartinier.killerparty.db.repository.PartyRepository
import com.fmartinier.killerparty.db.repository.PlayerRepository
import com.fmartinier.killerparty.db.repository.PlayerToChallengeRepository
import com.fmartinier.killerparty.model.Challenge
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.model.enums.PartyState
import com.fmartinier.killerparty.utils.navigateTo
import java.time.Instant
import kotlin.random.Random


class PartyService(val context: Context) {
    private val partyRepository = PartyRepository(context)
    private val challengeRepository =
        ChallengeRepository(context)
    private val playerRepository = PlayerRepository(context)
    private val playerToChallengeRepository = PlayerToChallengeRepository(context)
    private val smsService = SmsService(context)

    fun findOrCreate(): Party {
        return partyRepository.findOrCreate()
    }

    fun findAllBegan(): List<Party> {
        return partyRepository.findAll().filter { it.state != PartyState.NOT_STARTED }
    }

    fun beginParty(party: Party, players: List<Player>) {
        giveChallengeToPlayers(players)
        val partyId = party.id
        partyRepository.modifyStateById(partyId, PartyState.IN_PROGRESS)
        partyRepository.modifyDateById(partyId, Instant.now())

        navigateTo(context, R.id.navigation_historic)
    }

    fun canBeginParty(context: Context, party: Party): Boolean {
        val players = findPlayers(party)
        val challenges = challengeRepository.findAll()
        return if (players.size < 3) {
            Toast.makeText(context, R.string.not_enought_players, Toast.LENGTH_SHORT).show()
            false
        } else if (challenges.size < players.size) {
            Toast.makeText(context, R.string.not_enought_challenges, Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    fun findPlayers(history: Party): List<Player> {
        return playerRepository.findAllFromParty(history)
    }

    fun declareWinner(player: Player, party: Party) {
        partyRepository.declareWinner(player, party)
    }

    /**
     * Give random challenge to each player, and insert them in DB
     */
    private fun giveChallengeToPlayers(players: List<Player>) {
        val availableChallenges: MutableList<Challenge> = mutableListOf()
        val availableTargets: MutableList<Player> = mutableListOf()
        availableChallenges.addAll(challengeRepository.findAll())
        availableTargets.addAll(players)
        players.forEach {

            // Select random challenge
            val randomChallengeIndex = Random.nextInt(availableChallenges.size)
            val randomChallenge = availableChallenges[randomChallengeIndex]
            availableChallenges.remove(randomChallenge)

            // Select random target
            var randomTargetIndex = Random.nextInt(availableTargets.size)
            var randomTarget = availableTargets[randomTargetIndex]
            while (randomTarget == it || playerToChallengeRepository.existByKillerAndTarget(
                    killer = randomTarget,
                    target = it
                )
            ) {
                randomTargetIndex = Random.nextInt(availableTargets.size)
                randomTarget = availableTargets[randomTargetIndex]
            }
            availableTargets.remove(randomTarget)

            smsService.sendSMS(
                it.phone,
                context.resources.getString(
                    R.string.sms_challenge_init,
                    randomTarget.name,
                    randomChallenge.description
                )
            )

            playerToChallengeRepository.insert(it.id, randomTarget.id, randomChallenge.id)
        }
    }
}