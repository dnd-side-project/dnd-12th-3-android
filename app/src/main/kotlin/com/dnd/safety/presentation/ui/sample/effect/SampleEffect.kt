package com.dnd.safety.presentation.ui.sample.effect

sealed interface SampleEffect {
    data object Idle : SampleEffect
    data class ShowToast(val message: String) : SampleEffect
    data class ShowError(val message: String) : SampleEffect
}