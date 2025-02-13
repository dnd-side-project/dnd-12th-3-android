package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.LawDistrict
import com.skydoves.sandwich.ApiResponse

interface LawDistrictRepository {

    suspend fun getLawDistricts(keyword: String): ApiResponse<List<LawDistrict>>
}