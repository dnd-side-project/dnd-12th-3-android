package com.dnd.safety.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LawDistrictResponse(
    val results: Results
) {

    @Serializable
    data class Results(
        val juso: List<Juso>
    )

    @Serializable
    data class Juso(
        val admCd: String,
        val rnMgtSn: String,
        val udrtYn: String,
        val buldMnnm: Long,
        val buldSlno: Long,

        val siNm: String,
        val sggNm: String,
        val emdNm: String,
        val liNm: String,

        val roadAddr: String,
    )
}