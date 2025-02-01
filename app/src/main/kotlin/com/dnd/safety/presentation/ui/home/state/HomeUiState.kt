package com.dnd.safety.presentation.ui.home.state

import androidx.compose.runtime.Immutable

@Immutable
data class HomeUiState(
    val isCurrentLocation: Boolean = true,
    val keyword: String = "",
)