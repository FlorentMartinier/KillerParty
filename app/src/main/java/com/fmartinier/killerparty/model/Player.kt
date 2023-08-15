package com.fmartinier.killerparty.model

import com.fmartinier.killerparty.model.enums.PlayerState

data class Player(
    val id: Int,
    val name: String,
    val phone: String,
    var state: PlayerState = PlayerState.IN_LIFE,
)