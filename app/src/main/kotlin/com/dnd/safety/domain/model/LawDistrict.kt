package com.dnd.safety.domain.model

data class LawDistrict(
    val address: String,
    val address2: String,
    val sido: String,
    val pointDto: PointDto
)

data class PointDto(
    val admCd: String,
    val rnMgtSn: String,
    val udrtYn: String,
    val buldMnnm: Long,
    val buldSlno: Long
)
