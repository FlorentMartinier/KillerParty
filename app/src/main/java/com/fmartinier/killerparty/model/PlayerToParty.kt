package com.fmartinier.killerparty.model

data class PlayerToParty(
    val id: Int,
    val player: Player,
    val party: Party,
)