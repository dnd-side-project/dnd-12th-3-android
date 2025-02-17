package com.dnd.safety.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this)
}

fun LocalDateTime.daysAgo(): Long {
    return ChronoUnit.DAYS.between(this, LocalDateTime.now())
}

fun LocalDate.formatLocalDateDot(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return format(formatter)
}