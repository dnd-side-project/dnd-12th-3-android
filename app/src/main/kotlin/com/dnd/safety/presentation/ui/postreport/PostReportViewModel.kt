package com.dnd.safety.presentation.ui.postreport

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dnd.safety.data.model.Location
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.domain.model.IncidentReport
import com.dnd.safety.domain.model.SearchResult
import com.dnd.safety.domain.repository.IncidentRepository
import com.dnd.safety.domain.usecase.CreateIncidentReportUseCase
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.navigation.utils.toRouteType
import com.dnd.safety.presentation.ui.postreport.effect.PostReportEffect
import com.dnd.safety.presentation.ui.postreport.effect.PostReportModalEffect
import com.dnd.safety.presentation.ui.postreport.state.PostReportState
import com.dnd.safety.utils.FileManager
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostReportViewModel @Inject constructor(
    private val incidentRepository: IncidentRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<PostReportState>(PostReportState.Loading)
    val state: StateFlow<PostReportState> = _state.asStateFlow()

    private val _effect = Channel<PostReportEffect>()
    val effect = _effect.receiveAsFlow()

    private val _modalEffect =
        MutableStateFlow<PostReportModalEffect>(PostReportModalEffect.Dismiss)
    val modalEffect: StateFlow<PostReportModalEffect> get() = _modalEffect

    private var currentMode = PostReportMode.CREATE

    fun initMode(mode: PostReportMode) {
        currentMode = mode
        _state.update {
            when (mode) {
                PostReportMode.CREATE -> PostReportState.Success(
                    imageUris = listOf(Uri.parse(savedStateHandle.toRoute<MainTabRoute.PostReport.Report>().url)),
                )

                PostReportMode.EDIT -> PostReportState.Success(
                    incident = savedStateHandle.toRouteType<MainTabRoute.PostReport.Edit, Incident>().incident
                )
            }
        }
    }

    fun updateContent(content: String) {
        _state.update {
            when (it) {
                is PostReportState.Loading -> it
                is PostReportState.Success -> it.copy(content = content)
            }
        }
    }

    fun updateCategory(category: IncidentCategory) {
        _state.update {
            when (it) {
                is PostReportState.Loading -> it
                is PostReportState.Success -> it.copy(selectedCategory = category)
            }
        }
    }

    fun updateSelectedMedia(uris: List<Uri>) {
        _state.update {
            when (it) {
                is PostReportState.Loading -> it
                is PostReportState.Success -> it.copy(
                    imageUris = uris,
                    fileUrls = emptyList()
                )
            }
        }
    }

    fun onCompleteClick() = viewModelScope.launch {
        val currentState = state.value as? PostReportState.Success ?: return@launch

        if (!validateInput(currentState)) {
            _effect.send(PostReportEffect.ShowToast("모든 정보를 입력해주세요"))
            return@launch
        }

        _state.update { currentState.copy(isLoading = true) }

        when (currentMode) {
            PostReportMode.CREATE -> createIncident(currentState)
            PostReportMode.EDIT -> updateIncident(currentState)
        }
    }

    private suspend fun createIncident(
        currentState: PostReportState.Success
    ) {
        val incidentReport = IncidentReport(
            description = currentState.content,
            disasterGroup = currentState.selectedCategory?.name ?: return,
            location = currentState.location,
            images = currentState.imageUris
        )

        incidentRepository.createIncident(incidentReport)
            .suspendOnSuccess {
                _effect.send(PostReportEffect.NavigateBack)
                _effect.send(PostReportEffect.ShowToast("사고 등록에 성공했습니다"))
            }
            .suspendOnFailure {
                _effect.send(PostReportEffect.ShowToast("사고 등록에 실패했습니다"))
                Log.e("PostReportViewModel", "Failed to create incident ${message()}")
            }
            .also {
                _state.update { currentState.copy(isLoading = false) }
            }
    }

    private suspend fun updateIncident(
        currentState: PostReportState.Success
    ) {
        val incidentReport = IncidentReport(
            description = currentState.content,
            disasterGroup = currentState.selectedCategory?.name ?: return,
            location = currentState.location,
            images = currentState.imageUris,
            fileUrls = currentState.fileUrls
        )

        incidentRepository.updateIncident(incidentReport)
            .suspendOnSuccess {
                _effect.send(PostReportEffect.NavigateBack)
                _effect.send(PostReportEffect.ShowToast("사고 수정에 성공했습니다"))
            }
            .suspendOnFailure {
                _effect.send(PostReportEffect.ShowToast("사고 수정에 실패했습니다"))
                Log.e("PostReportViewModel", "Failed to update incident ${message()}")
            }
            .also {
                _state.update { currentState.copy(isLoading = false) }
            }
    }

    private fun validateInput(state: PostReportState): Boolean {
        val data = state as? PostReportState.Success ?: return false
        return data.content.isNotBlank() &&
                data.selectedCategory != null &&
                data.location.placeName.isNotBlank()
    }

    fun addressSelected(searchResult: SearchResult) {
        _state.update {
            when (it) {
                is PostReportState.Loading -> it
                is PostReportState.Success -> it.copy(
                    location = Location(
                        latitude = searchResult.point.y,
                        longitude = searchResult.point.x,
                        placeName = searchResult.name,
                        roadNameAddress = searchResult.roadAddress,
                        lotNumberAddress = searchResult.lotAddress
                    ),
                )
            }
        }
    }

    fun showPhotoPicker() {
        _modalEffect.update { PostReportModalEffect.ShowPhotoPickerDialog }
    }

    fun showSearchDialog() {
        _modalEffect.update { PostReportModalEffect.ShowSearchDialog }
    }

    fun dismissModal() {
        _modalEffect.update { PostReportModalEffect.Dismiss }
    }
}
