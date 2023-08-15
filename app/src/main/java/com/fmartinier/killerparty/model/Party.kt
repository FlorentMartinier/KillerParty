package com.fmartinier.killerparty.model

import com.fmartinier.killerparty.model.enums.PartyState
import java.time.LocalDate

data class Party(
    val id: Int,
    val date: LocalDate? = LocalDate.now(),
    val state: PartyState = PartyState.NOT_STARTED,
    var winner: String? = null,
)