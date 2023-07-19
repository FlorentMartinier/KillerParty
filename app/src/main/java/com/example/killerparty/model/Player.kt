package com.example.killerparty.model

import com.example.killerparty.model.enums.PlayerState

data class Player(
    val id: Int,
    val name: String,
    val phone: String,
    val state: PlayerState = PlayerState.IN_LIFE,
)