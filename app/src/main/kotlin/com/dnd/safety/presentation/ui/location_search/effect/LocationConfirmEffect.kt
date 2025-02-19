package com.dnd.safety.presentation.ui.location_search.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class LocationConfirmEffect {

    @Immutable
    data object NavigateToMainScreen : LocationConfirmEffect()

    @Immutable
    data class ShowSnackBar(val message: String) : LocationConfirmEffect()
}
