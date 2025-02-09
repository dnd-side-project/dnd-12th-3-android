package com.dnd.safety.presentation.ui.postreport

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.presentation.ui.photoselection.PhotoSelectionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostReportViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val selectedMediaItems =
        savedStateHandle.getStateFlow<List<Uri>>("selectedMedia", emptyList())

    private val _state = MutableStateFlow(PostReportState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PostReportEffect>()
    val effect = _effect.receiveAsFlow()


    init {
        viewModelScope.launch {
            selectedMediaItems.collect { uris ->
                Log.d("PostReport", "Received URIs in selectedMediaItems: $uris")
                _state.update { it.copy(imageUris = uris) }
            }
        }

        Log.d(
            "PostReport", "Direct savedStateHandle value: ${
                savedStateHandle.get<List<Uri>>(
                    PhotoSelectionViewModel.KEY_SELECTED_MEDIA
                )
            }"
        )
    }

    fun updateContent(content: String) {
        _state.update { it.copy(content = content) }
    }

    fun updateCategory(category: IncidentCategory) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun navigateToPhotoSelection() = viewModelScope.launch {
        _effect.send(PostReportEffect.NavigateToPhotoSelection)
    }

    fun updateSelectedMedia(uris: List<Uri>) {
        _state.update { it.copy(imageUris = uris) }
    }

    fun onLocationClick() {
        // TODO: 위치 선택 화면으로 이동
    }

    fun onCompleteClick() {
        // TODO: 글 작성 완료 처리
        viewModelScope.launch {
            _effect.send(PostReportEffect.NavigateBack)
        }
    }
}
