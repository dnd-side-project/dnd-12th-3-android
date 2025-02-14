package com.dnd.safety.presentation.ui.location_search.effect

sealed class LocationConfirmEffect {
    object ShowPermissionDeniedMessage : LocationConfirmEffect()
    object NavigateToMainScreen : LocationConfirmEffect()
}
