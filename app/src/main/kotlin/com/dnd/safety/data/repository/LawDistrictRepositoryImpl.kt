package com.dnd.safety.data.repository

import com.dnd.safety.data.remote.api.LawDistrictService
import com.dnd.safety.domain.mapper.toLawDistricts
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.repository.LawDistrictRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import javax.inject.Inject

class LawDistrictRepositoryImpl @Inject constructor(
    private val lawDistrictService: LawDistrictService
) : LawDistrictRepository {

    override suspend fun getLawDistricts(keyword: String): ApiResponse<List<LawDistrict>> {
        return lawDistrictService.getLawDistricts(keyword)
            .mapSuccess {
                toLawDistricts()
            }
    }
}