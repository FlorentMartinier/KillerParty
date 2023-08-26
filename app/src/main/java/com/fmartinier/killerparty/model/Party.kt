package com.fmartinier.killerparty.model

import com.fmartinier.killerparty.model.enums.PartyState
import java.time.Instant

data class Party(
    val id: Int,
    val date: Instant? = Instant.now(),
    val state: PartyState = PartyState.NOT_STARTED,
    var winner: String? = null,
)