package com.example.killerparty.model

import java.time.LocalDate

data class Historic(
    val id: String,
    val date: LocalDate?,
    val winner: String?,
    val players: List<Player>,
)