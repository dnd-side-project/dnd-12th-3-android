package com.dnd.safety.domain.model

data class SearchResult(
    val address: String,
    val name: String,
    val point: Point
)