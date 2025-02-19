package com.dnd.safety.domain.model

data class MyReports(
    val incidents: List<Incident>,
    val nextCursor: Long
)