package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LawDistrictResponse(
    val results: Results
) {

    @Serializable
    data class Results(
        val juso: List<JusoDto>
    )

    @Serializable
    data class JusoDto(
        val admCd: String,
        val rnMgtSn: String,
        val udrtYn: String,
        val bdNm: String,
        val buldMnnm: Long,
        val buldSlno: Long,

        val siNm: String,
        val sggNm: String,
        val emdNm: String,
        val liNm: String,

        val roadAddr: String,
        val roadAddrPart1: String,
        val jibunAddr: String,
    )
}


@Serializable
data class LawPointResponse(
    val results: Results
) {

    @Serializable
    data class Results(
        val juso: List<JusoDto>
    )

    @Serializable
    data class JusoDto(
        val entX: Double,
        val entY: Double,
    )
}