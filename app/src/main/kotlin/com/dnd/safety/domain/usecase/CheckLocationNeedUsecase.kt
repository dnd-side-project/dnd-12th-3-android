package com.dnd.safety.domain.usecase

import com.dnd.safety.domain.repository.MyTownRepository
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class CheckLocationNeedUsecase @Inject constructor(
    private val myTownRepository: MyTownRepository
) {

    suspend operator fun invoke(): Boolean {
        return when (val myTown = myTownRepository.getMyTownList()) {
            is ApiResponse.Success -> myTown.data.isEmpty()
            else -> false
        }
    }
}