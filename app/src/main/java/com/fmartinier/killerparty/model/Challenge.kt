package com.fmartinier.killerparty.model

data class Challenge(
    val id: Int,
    val description: String,
    val enable: Boolean = true,
)