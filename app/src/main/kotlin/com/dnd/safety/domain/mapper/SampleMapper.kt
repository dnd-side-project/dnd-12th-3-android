package com.dnd.safety.domain.mapper

import com.dnd.safety.data.local.entity.SampleEntity
import com.dnd.safety.domain.model.Sample
import com.dnd.safety.data.model.request.SampleRequest
import com.dnd.safety.data.model.response.SampleResponse

// Response -> Domain
fun SampleResponse.toDomain() = Sample(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt,
)

// Entity -> Domain
fun SampleEntity.toDomain() = Sample(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt,
)

// Domain -> Entity
fun Sample.toEntity() = SampleEntity(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt,
)

// Domain -> Request
fun Sample.toRequest() = SampleRequest(
    id = id,
    name = name,
)