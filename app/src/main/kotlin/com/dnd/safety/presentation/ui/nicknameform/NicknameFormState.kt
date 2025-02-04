package com.dnd.safety.presentation.ui.nicknameform

data class NicknameFormState(
    val text: String = "",
    val isLoading: Boolean = false,
) {
    val isButtonEnabled: Boolean
        get() = text.length in 1..10
}