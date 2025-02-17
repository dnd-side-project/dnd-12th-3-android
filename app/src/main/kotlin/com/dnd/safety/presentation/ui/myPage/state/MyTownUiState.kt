package com.dnd.safety.presentation.ui.myPage.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.dnd.safety.domain.model.MyTown

@Stable
sealed interface MyTownUiState {

    @Immutable
    data object Loading : MyTownUiState

    @Immutable
    data class Success(
        val myTowns: List<MyTown>
    ) : MyTownUiState {

        val firstMyTown: MyTown?
            get() = myTowns.firstOrNull()

        val secondMyTown: MyTown?
            get() = myTowns.getOrNull(1)

        val selectedLocation = myTowns.find { it.selected }?.latLng
    }
}