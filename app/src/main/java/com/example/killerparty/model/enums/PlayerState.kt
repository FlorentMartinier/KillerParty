package com.example.killerparty.model.enums

import com.example.killerparty.R

enum class PlayerState {
    KILLED,
    IN_LIFE;

    fun translate(): Int {
        return when(this){
            KILLED -> R.string.player_killed
            else -> R.string.player_in_life
        }
    }
}