package com.dnd.safety.presentation.ui.postreport.effect

sealed class PhotoSelectionEffect {
    data object NavigateBack : PhotoSelectionEffect()
    data class ShowToast(val message: String) : PhotoSelectionEffect()
    data object RequestPermission : PhotoSelectionEffect()
}
