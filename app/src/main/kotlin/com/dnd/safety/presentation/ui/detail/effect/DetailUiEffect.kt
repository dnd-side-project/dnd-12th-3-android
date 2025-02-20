package com.dnd.safety.presentation.ui.detail.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface DetailUiEffect {

    @Immutable
    data class ShowSnackBar(val message: String) : DetailUiEffect

    @Immutable
    data object HideKeyboard : DetailUiEffect
}