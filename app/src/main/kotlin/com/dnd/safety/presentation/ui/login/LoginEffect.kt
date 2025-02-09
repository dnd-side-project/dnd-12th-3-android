package com.dnd.safety.presentation.ui.login

sealed class LoginEffect  {
    data class ShowToast(val message: String): LoginEffect()
    object NavigateToNickName: LoginEffect()
}