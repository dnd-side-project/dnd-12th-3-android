package com.dnd.safety.domain.usecase

import com.dnd.safety.domain.model.Sample
import com.dnd.safety.domain.repository.SampleRepository
import javax.inject.Inject

class GetSamplesUseCase @Inject constructor(
    private val repository: SampleRepository
) {
    suspend operator fun invoke(): List<Sample> {
        return repository.getSamples()
    }
}