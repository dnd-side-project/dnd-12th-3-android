package com.dnd.safety.utils

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this)
}

fun LocalDate.formatLocalDateDot(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return format(formatter)
}

fun LocalDateTime.daysAgo(): String {
    val now = LocalDateTime.now()
    val duration = Duration.between(this, now)

    return when {
        duration.toDays() > 0 -> "${duration.toDays()}일 전"
        duration.toHours() > 0 -> "${duration.toHours()}시간 전"
        duration.toMinutes() > 0 -> "${duration.toMinutes()}분 전"
        else -> "방금 전"
    }
}