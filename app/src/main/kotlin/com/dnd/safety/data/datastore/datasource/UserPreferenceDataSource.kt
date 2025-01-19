package com.dnd.safety.data.datastore.datasource

import androidx.datastore.core.DataStore
import com.dnd.safety.data.datastore.model.UserPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferenceDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {

    val userPreferenceDataFlow = userPreferences.data

    suspend fun getUserPreferenceData() = userPreferenceDataFlow.first()

}