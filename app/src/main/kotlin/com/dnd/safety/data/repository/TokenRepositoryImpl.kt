package com.dnd.safety.data.repository

import com.dnd.safety.data.datastore.datasource.UserPreferenceDataSource
import com.dnd.safety.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val userPreferenceDataSource: UserPreferenceDataSource
) : TokenRepository {

    override suspend fun getToken(): String {
        return userPreferenceDataSource.getToken()
    }

    override suspend fun setToken(token: String) {
        userPreferenceDataSource.setToken(token)
    }
}