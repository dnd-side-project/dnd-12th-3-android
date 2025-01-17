package com.dnd.safety.data.repository

import com.dnd.safety.data.local.dao.SampleDao
import com.dnd.safety.data.remote.api.SampleApi
import com.dnd.safety.domain.mapper.toDomain
import com.dnd.safety.domain.model.Sample
import com.dnd.safety.domain.repository.SampleRepository
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class SampleRepositoryImpl @Inject constructor(
    private val sampleApi: SampleApi,
    private val sampleDao: SampleDao,
) : SampleRepository {
    override suspend fun getSamples(): List<Sample> =
        when (val response = sampleApi.getSamples()) {
            is ApiResponse.Success -> {
                response.data.map { it.toDomain() }
            }
            is ApiResponse.Failure -> {
                emptyList()
            }
        }
}