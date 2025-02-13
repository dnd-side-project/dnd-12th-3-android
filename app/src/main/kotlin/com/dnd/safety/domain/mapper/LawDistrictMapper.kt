package com.dnd.safety.domain.mapper

import com.dnd.safety.data.model.response.LawDistrictResponse
import com.dnd.safety.domain.model.LawDistrict

fun LawDistrictResponse.toLawDistricts() = results.juso.map {
    LawDistrict(
        address = it.roadAddr,
        sido = listOf(
            it.siNm,
            it.sggNm,
            it.emdNm,
            it.liNm,
        ).joinToString(" "),
        admCd = it.admCd,
        rnMgtSn = it.rnMgtSn,
        udrtYn = it.udrtYn,
        buldMnnm = it.buldMnnm,
        buldSlno = it.buldSlno
    )
}