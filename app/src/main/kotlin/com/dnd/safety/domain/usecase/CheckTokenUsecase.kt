package com.dnd.safety.domain.usecase

import com.dnd.safety.data.datasorce.datastore.datasource.UserPreferenceDataSource
import com.dnd.safety.utils.Const
import javax.inject.Inject

class CheckTokenUsecase @Inject constructor(
    private val userPreferenceDataSource: UserPreferenceDataSource
) {

    suspend operator fun invoke(): Boolean {
        return userPreferenceDataSource.getToken() != Const.DEFAULT_TOKEN
    }
}