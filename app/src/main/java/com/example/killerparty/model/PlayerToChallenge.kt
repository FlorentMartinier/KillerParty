package com.example.killerparty.model

data class PlayerToChallenge(
    val id: String,
    val challenge: Challenge,
    val hunter: Player,
    val target: Player,
)