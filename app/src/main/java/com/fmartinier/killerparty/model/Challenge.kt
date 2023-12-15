package com.fmartinier.killerparty.model

data class Challenge(
    val id: Int,
    val description: String,
    var enable: Boolean = true,
)