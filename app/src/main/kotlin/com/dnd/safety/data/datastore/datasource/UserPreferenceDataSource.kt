package com.dnd.safety.data.datastore.datasource

import androidx.datastore.core.DataStore
import com.dnd.safety.data.datastore.model.UserPreferences
import com.dnd.safety.utils.Const.DEFAULT_TOKEN
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UserPreferenceDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {

    val userPreferenceDataFlow = userPreferences.data.catch { emit(UserPreferences()) }

    suspend fun getToken() = userPreferenceDataFlow.firstOrNull()?.token ?: DEFAULT_TOKEN

    suspend fun setUserInfo(token: String, name: String) {
        userPreferences.updateData { preferences ->
            preferences.copy(
                token = token,
                name = name
            )
        }
    }

    suspend fun getName() = userPreferenceDataFlow.firstOrNull()?.name ?: ""
}