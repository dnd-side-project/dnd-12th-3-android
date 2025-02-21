package com.dnd.safety.data.repository

import com.dnd.safety.data.local.datastore.datasource.UserPreferenceDataSource
import com.dnd.safety.domain.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val userPreferenceDataSource: UserPreferenceDataSource
) : UserInfoRepository {

    override suspend fun setUserInfo(token: String, name: String) {
        userPreferenceDataSource.setUserInfo(token, name)
    }

    override suspend fun getToken(): String {
        return userPreferenceDataSource.getToken()
    }

    override suspend fun getName(): String {
        return userPreferenceDataSource.getName()
    }
}