package com.dnd.safety.presentation.ui.location_search.effect

sealed class LocationConfirmEffect {
    data object NavigateToMainScreen : LocationConfirmEffect()
}
