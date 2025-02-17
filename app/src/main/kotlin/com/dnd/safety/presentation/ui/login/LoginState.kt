package com.dnd.safety.presentation.ui.login

sealed interface LoginUiState {

    data object Initializing : LoginUiState

    data object NeedLogin : LoginUiState
}