package com.dnd.safety.presentation.ui.photoselection

sealed class PhotoSelectionEffect {
    object NavigateBack : PhotoSelectionEffect()
    data class ShowToast(val message: String) : PhotoSelectionEffect()
    object RequestPermission : PhotoSelectionEffect()
}
