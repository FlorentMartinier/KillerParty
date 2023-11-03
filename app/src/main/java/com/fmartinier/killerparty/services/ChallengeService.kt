package com.fmartinier.killerparty.services

import android.content.Context
import com.fmartinier.killerparty.db.repository.ChallengeRepository
import com.fmartinier.killerparty.model.Challenge
import com.fmartinier.killerparty.model.Player

class ChallengeService(context: Context) {
    private val challengeRepository = ChallengeRepository(context)

    fun insert(description: String) {
        challengeRepository.insert(description)
    }

    fun deleteById(id: Int) {
        challengeRepository.deleteById(id)
    }

    fun findAll(): List<Challenge> {
        return challengeRepository.findAll()
    }

    fun findAllEnabled(): List<Challenge> {
        return challengeRepository.findAll().filter { it.enable }
    }

    fun setEnableById(id: Int, value: Boolean) {
        challengeRepository.setEnableById(id, value)
    }

    fun findActiveFromPlayer(player: Player): Challenge {
        return challengeRepository.findActiveFromPlayer(player)
    }
}