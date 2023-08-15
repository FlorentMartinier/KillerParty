package com.example.killerparty.services

import android.content.Context
import com.example.killerparty.db.repository.ChallengeRepository
import com.example.killerparty.model.Challenge
import com.example.killerparty.model.Player

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

    fun findActiveFromPlayer(player: Player): Challenge {
        return challengeRepository.findActiveFromPlayer(player)
    }
}