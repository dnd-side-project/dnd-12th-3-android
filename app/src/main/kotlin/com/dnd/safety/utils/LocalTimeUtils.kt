package com.dnd.safety.utils

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this)
}

fun LocalDateTime.daysAgo(): Long {
    return ChronoUnit.DAYS.between(this, LocalDateTime.now())
}
