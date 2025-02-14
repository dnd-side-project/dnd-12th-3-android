package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.model.PointDto
import com.skydoves.sandwich.ApiResponse

interface LawDistrictRepository {

    suspend fun getLawDistricts(keyword: String): ApiResponse<List<LawDistrict>>

    suspend fun getPoint(pointDto: PointDto): ApiResponse<Point>
}