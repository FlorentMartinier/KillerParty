package com.example.killerparty.model.enums

import com.example.killerparty.R

enum class PartyState {
    NOT_STARTED,
    IN_PROGRESS,
    ENDED;

    fun translate(): Int {
        return when(this){
            NOT_STARTED -> R.string.party_state_not_started
            IN_PROGRESS -> R.string.party_state_in_progress
            else -> R.string.party_state_ended
        }
    }
}