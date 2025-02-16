package com.dnd.safety.presentation.ui.location_search.effect

import com.dnd.safety.domain.model.MyTown

sealed class LocationSearchEffect {
    data class NavigateToLocationConfirm(val myTown: MyTown) : LocationSearchEffect()
    data class ShowToast(val message: String) : LocationSearchEffect()
}