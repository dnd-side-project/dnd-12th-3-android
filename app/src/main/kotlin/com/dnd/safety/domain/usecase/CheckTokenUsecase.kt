package com.dnd.safety.domain.usecase

import com.dnd.safety.data.datasorce.datastore.datasource.UserPreferenceDataSource
import com.dnd.safety.utils.Const
import com.dnd.safety.utils.Logger
import javax.inject.Inject

class CheckTokenUsecase @Inject constructor(
    private val userPreferenceDataSource: UserPreferenceDataSource
) {

    suspend operator fun invoke(): Boolean {
        return userPreferenceDataSource.getToken().also {
            Logger.d("token: $it")
        } != Const.DEFAULT_TOKEN
    }
}