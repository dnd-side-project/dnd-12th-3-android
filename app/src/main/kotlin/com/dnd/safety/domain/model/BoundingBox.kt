package com.dnd.safety.domain.model

data class BoundingBox(
    val topRight: Point,  // 우측 상단
    val bottomleft: Point // 좌측 하단
)
