package com.fmartinier.killerparty.extensions

import java.time.Duration
import java.time.Instant

fun Instant.isNearOf(instant: Instant): Boolean {
    return Duration.between(this, instant).toMillis() < 1000
}