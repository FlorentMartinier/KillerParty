package com.fmartinier.killerparty.model.enums

import com.fmartinier.killerparty.R

enum class PlayerState {
    KILLED,
    ALIVE;

    fun translate(): Int {
        return when (this) {
            KILLED -> R.string.player_killed
            else -> R.string.player_alive
        }
    }
}