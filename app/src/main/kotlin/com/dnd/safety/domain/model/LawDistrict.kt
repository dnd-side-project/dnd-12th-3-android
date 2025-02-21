package com.dnd.safety.domain.model

data class LawDistrict(
    val address: String,
    val roadAddress: String,
    val lotAddress: String,
    val sido: String,
    val sgg: String,
    val emd: String,
    val name: String,
    val pointDto: PointDto
)

data class PointDto(
    val admCd: String,
    val rnMgtSn: String,
    val udrtYn: String,
    val buldMnnm: Long,
    val buldSlno: Long
)
