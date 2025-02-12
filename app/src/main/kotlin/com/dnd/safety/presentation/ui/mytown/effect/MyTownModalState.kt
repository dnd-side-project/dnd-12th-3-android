package com.dnd.safety.presentation.ui.mytown.effect

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface MyTownModalState {

    @Immutable
    data object Dismiss : MyTownModalState

    @Immutable
    data object ShowSearchDialog : MyTownModalState
}