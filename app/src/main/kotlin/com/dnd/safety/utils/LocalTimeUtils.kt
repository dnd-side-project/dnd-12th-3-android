package com.dnd.safety.utils

import java.time.LocalDateTime

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this)
}