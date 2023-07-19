package com.example.killerparty.model

import com.example.killerparty.model.enums.PartyState

data class Party(
    val id: Int,
    val state: PartyState = PartyState.NOT_STARTED,
)