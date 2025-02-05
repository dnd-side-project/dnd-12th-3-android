package com.dnd.safety.presentation.ui.locationconfirm

sealed class LocationConfirmEffect {
    object ShowPermissionDeniedMessage : LocationConfirmEffect()
    object NavigateToMainScreen : LocationConfirmEffect()
}
