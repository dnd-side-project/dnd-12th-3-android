package com.dnd.safety.presentation.ui.login

data class LoginState(
    val isLoading: Boolean = false,
    val token: String? = null,
)