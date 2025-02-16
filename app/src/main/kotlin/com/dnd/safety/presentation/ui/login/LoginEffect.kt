package com.dnd.safety.presentation.ui.login

sealed interface LoginEffect  {

    data class ShowSnackBar(val message: String): LoginEffect

    data object NavigateToLocation: LoginEffect

    data object NavigateToHome: LoginEffect

}