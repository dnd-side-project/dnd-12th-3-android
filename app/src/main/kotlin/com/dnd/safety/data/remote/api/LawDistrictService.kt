package com.dnd.safety.data.remote.api

import com.dnd.safety.BuildConfig
import com.dnd.safety.data.model.response.LawDistrictResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LawDistrictService {

    @GET("addrLinkApi.do")
    suspend fun getLawDistricts(
        @Query("keyword") keyword: String,
        @Query("confmKey") confmKey: String = BuildConfig.ADDRESS_SEARCH_KEY,
        @Query("resultType") resultType: String = "json",
        @Query("currentPage") currentPage: Int = 1,
        @Query("countPerPage") countPerPage: Int = 10,
    ): ApiResponse<LawDistrictResponse>
}