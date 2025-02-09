package com.dnd.safety.domain.model

enum class Sort(val text: String) {
    LATEST("최신순"),
    DISTANCE("거리순"),
    VIEW("조회순"),
    REGISTERED("등록시간순"),
    COMMENT("댓글순")
}

data class SortFilter(
    val sort: Sort,
    val isSelected: Boolean
) {

    companion object {
        val entries = Sort.entries.map {
            SortFilter(
                sort = it,
                isSelected = it == Sort.LATEST
            )
        }
    }
}