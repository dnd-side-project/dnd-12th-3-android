package com.dnd.safety.presentation.ui.locationsearch

import com.dnd.safety.data.model.Location

sealed class LocationSearchEffect {
    data class NavigateToLocationConfirm(val location: Location) : LocationSearchEffect()
    data class ShowToast(val message: String) : LocationSearchEffect()
}