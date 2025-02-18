package com.dnd.safety.presentation.ui.myPage.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface MyTownUiEffect {

    @Immutable
    data class ShowSnackBar(val message: String) : MyTownUiEffect
}