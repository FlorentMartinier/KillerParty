package com.example.killerparty.model

import com.example.killerparty.model.enums.PlayerToChallengeState

data class PlayerToChallenge(
    val id: Int,
    val challengeId: Int,
    val killerId: Int,
    val targetId: Int,
    val state: PlayerToChallengeState,
)