package com.dnd.safety.data.model.response

data class LocationResponse(
    val meta: Meta,
    val documents: List<Document>
)

data class Meta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
)

data class Document(
    val place_name: String,
    val address_name: String,
    val x: String,
    val y: String
)