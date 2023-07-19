package com.example.killerparty.model

data class PlayerToChallenge(
    val id: Int,
    val challenge: Challenge,
    val target: Player,
)