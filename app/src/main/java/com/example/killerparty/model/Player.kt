package com.example.killerparty.model

import com.example.killerparty.model.enums.PlayerState

data class Player(
    val id: String,
    val name: String,
    val phone: String,
    val state: PlayerState,
)