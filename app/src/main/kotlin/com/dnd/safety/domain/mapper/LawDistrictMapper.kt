package com.dnd.safety.domain.mapper

import com.dnd.safety.data.model.response.LawDistrictResponse
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.PointDto

fun LawDistrictResponse.toLawDistricts() = results.juso.map {
    LawDistrict(
        address = it.roadAddr,
        address2 = it.roadAddrPart1,
        sido = listOf(
            it.siNm,
            it.sggNm,
            it.emdNm,
            it.liNm,
        ).joinToString(" "),
        name = it.bdNm,
        pointDto = PointDto(
            admCd = it.admCd,
            rnMgtSn = it.rnMgtSn,
            udrtYn = it.udrtYn,
            buldMnnm = it.buldMnnm,
            buldSlno = it.buldSlno
        )
    )
}