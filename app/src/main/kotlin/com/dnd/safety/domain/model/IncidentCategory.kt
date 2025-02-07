package com.dnd.safety.domain.model

enum class IncidentCategory(val korTitle: String) {
    ALL("전체"),
    TRAFFIC("교통"),
    FIRE("화재"),
    COLLAPSE("붕괴"),
    EXPLOSION("폭발"),
    NATURAL("자연재난"),
    DUST("미세먼지"),
    TERROR("테러");

    companion object {
        fun getAllExceptAll(): List<IncidentCategory> {
            return entries.filter { it != ALL }
        }

        fun getAll(): List<IncidentCategory> {
            return entries.toList()
        }
    }
}
