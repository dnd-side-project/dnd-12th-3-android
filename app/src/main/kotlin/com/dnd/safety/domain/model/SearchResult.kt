package com.dnd.safety.domain.model

data class SearchResult(
    val name: String,
    val roadAddress: String,
    val lotAddress: String,
    val point: Point,
    val sido: String,
    val sgg: String,
    val emd: String
)