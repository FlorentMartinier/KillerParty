package com.example.killerparty.services

import android.content.Context
import com.example.killerparty.db.repository.ChallengeRepository
import com.example.killerparty.model.Challenge

class ChallengeService(context: Context) {
    private val challengeRepository = ChallengeRepository(context)

    fun insertChallenge(description: String) {
        challengeRepository.insertChallenge(description)
    }

    fun deleteChallengeById(id: Int) {
        challengeRepository.deleteChallengeById(id)
    }

    fun findAllChallenges(): List<Challenge> {
        return challengeRepository.findAllChallenges()
    }
}