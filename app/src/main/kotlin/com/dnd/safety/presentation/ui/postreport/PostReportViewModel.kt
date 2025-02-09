package com.dnd.safety.presentation.ui.postreport

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.domain.model.IncidentReport
import com.dnd.safety.domain.usecase.CreateIncidentReportUseCase
import com.dnd.safety.domain.usecase.GetCurrentLocationUseCase
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
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val createIncidentReportUseCase: CreateIncidentReportUseCase,
) : ViewModel() {
    private val selectedMediaItems =
        savedStateHandle.getStateFlow<List<Uri>>("selectedMedia", emptyList())

    private val _state = MutableStateFlow(PostReportState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PostReportEffect>()
    val effect = _effect.receiveAsFlow()

    fun fetchCurrentLocation() = viewModelScope.launch {
        getCurrentLocationUseCase()
            .onSuccess { location ->
                _state.update { it.copy(location = location) }
            }
            .onFailure { throwable ->
                Log.e("PostReportViewModel", "위치 정보 가져오기 실패", throwable)
            }
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

    fun onCompleteClick() = viewModelScope.launch {
        val currentState = _state.value

        if (!validateInput(currentState)) {
            _effect.send(PostReportEffect.ShowToast("모든 필드를 입력해주세요"))
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val incidentReport = IncidentReport(
            writerId = 1, // TODO: Get actual user ID
            description = currentState.content,
            disasterGroup = currentState.selectedCategory?.name ?: return@launch,
            location = currentState.location,
            images = currentState.imageUris
        )

        createIncidentReportUseCase(incidentReport)
            .onSuccess {
                _effect.send(PostReportEffect.NavigateBack)
            }
            .onFailure { throwable ->
                _effect.send(PostReportEffect.ShowToast("사고 등록에 실패했습니다"))
                Log.e("PostReportViewModel", "Failed to create incident", throwable)
            }
            .also {
                _state.update { it.copy(isLoading = false) }
            }
    }

    private fun validateInput(state: PostReportState): Boolean {
        return state.content.isNotBlank() &&
                state.selectedCategory != null &&
                state.location.address.isNotBlank()
    }
}
