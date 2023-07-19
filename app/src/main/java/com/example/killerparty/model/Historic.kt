package com.example.killerparty.model

import java.time.LocalDate

data class Historic(
    val id: Int,
    val date: LocalDate?,
    val winner: String?,
    val party: Party,
)