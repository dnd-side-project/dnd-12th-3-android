package com.dnd.safety.domain.repository

import com.dnd.safety.domain.model.Sample

interface SampleRepository {
    suspend fun getSamples(): List<Sample>
}