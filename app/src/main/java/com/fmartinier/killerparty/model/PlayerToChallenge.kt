package com.fmartinier.killerparty.model

import com.fmartinier.killerparty.model.enums.PlayerToChallengeState

data class PlayerToChallenge(
    val id: Int? = null,
    val challengeId: Int,
    val killerId: Int,
    val targetId: Int,
    val state: PlayerToChallengeState = PlayerToChallengeState.EMPTY,
)