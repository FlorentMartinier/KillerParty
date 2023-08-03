package com.example.killerparty.model

import com.example.killerparty.model.enums.PlayerToChallengeState

data class PlayerToChallenge(
    val id: Int,
    val challenge: Challenge,
    val target: Player,
    val state: PlayerToChallengeState,
)