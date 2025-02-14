package com.dnd.safety.presentation.ui.postreport.photoSelection

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dnd.safety.domain.model.Media
import com.dnd.safety.domain.usecase.GetMediaFromGalleryUseCase
import com.dnd.safety.presentation.navigation.MainTabRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoSelectionViewModel @Inject constructor(
    private val getMediaFromGalleryUseCase: GetMediaFromGalleryUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(PhotoSelectionState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PhotoSelectionEffect>()
    val effect = _effect.receiveAsFlow()

    private var tempPhotoUri: Uri? = null

    fun setTempPhotoUri(uri: Uri) {
        tempPhotoUri = uri
    }

    fun loadMedia() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        try {
            val mediaFlow = getMediaFromGalleryUseCase().cachedIn(viewModelScope)
            _state.update {
                it.copy(
                    mediaFlow = mediaFlow,
                    isLoading = false,
                )
            }
        } catch (e: Exception) {
            _effect.send(PhotoSelectionEffect.ShowToast(e.message ?: "미디어 로딩 중 오류가 발생했습니다"))
            _state.update { it.copy(isLoading = false) }
        }
    }


    fun onMediaSelected(media: Media) {
        _state.update { state ->
            val currentSelected = state.selectedMedia.toMutableList()
            if (currentSelected.contains(media)) {
                currentSelected.remove(media)
            } else if (currentSelected.size < 3) {
                currentSelected.add(media)
            }
            state.copy(selectedMedia = currentSelected)
        }
    }

    fun onConfirmClick() = viewModelScope.launch {
        _effect.send(PhotoSelectionEffect.NavigateBack)
    }

    fun onPermissionResult(isGranted: Boolean) = viewModelScope.launch {
        _state.update { it.copy(isPermissionGranted = isGranted) }
        if (!isGranted) {
            _effect.send(PhotoSelectionEffect.RequestPermission)
        }
    }

    companion object {
        const val KEY_SELECTED_MEDIA = "selected_media"
    }
}