package com.example.killerparty.model

import com.example.killerparty.model.enums.PartyState
import java.time.LocalDate

data class Party(
    val id: Int,
    val date: LocalDate? = LocalDate.now(),
    val state: PartyState = PartyState.NOT_STARTED,
    val winner: String? = null,
)