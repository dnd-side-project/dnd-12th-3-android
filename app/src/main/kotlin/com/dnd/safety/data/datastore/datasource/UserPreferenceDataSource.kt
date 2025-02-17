package com.dnd.safety.data.datastore.datasource

import androidx.datastore.core.DataStore
import com.dnd.safety.data.datastore.model.UserPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UserPreferenceDataSource @Inject constructor(
    userPreferences: DataStore<UserPreferences>,
) {

    val userPreferenceDataFlow = userPreferences.data.catch { emit(UserPreferences()) }

    suspend fun getToken() = userPreferenceDataFlow.firstOrNull()?.token ?: UserPreferences.DEFAULT_TOKEN

}