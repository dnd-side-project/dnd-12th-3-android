package com.dnd.safety.presentation.ui.myPage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.repository.IncidentRepository
import com.dnd.safety.domain.repository.MyReportRepository
import com.dnd.safety.presentation.ui.detail.effect.DetailModalEffect
import com.dnd.safety.presentation.ui.detail.effect.DetailUiEffect
import com.dnd.safety.presentation.ui.myPage.effect.MyReportModalEffect
import com.dnd.safety.presentation.ui.myPage.effect.MyReportUiEffect
import com.dnd.safety.presentation.ui.myPage.state.MyReportListState
import com.dnd.safety.utils.trigger.TriggerStateFlow
import com.dnd.safety.utils.trigger.triggerStateIn
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReportViewModel @Inject constructor(
    private val myReportRepository: MyReportRepository,
    private val incidentRepository: IncidentRepository
) : ViewModel() {

    var isRefreshing = mutableStateOf(false)
        private set

    var cursor = MutableStateFlow<Long?>(null)

    val myReportListState: TriggerStateFlow<MyReportListState> = cursor.map {
        val result = myReportRepository.getMyReports(it)
        isRefreshing.value = false

        when (result) {
            is ApiResponse.Success -> MyReportListState.Success(result.data.incidents, result.data.nextCursor)
            is ApiResponse.Failure -> MyReportListState.Empty
        }
    }.triggerStateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MyReportListState.Loading
    )

    private val _myReportModalEffect = MutableStateFlow<MyReportModalEffect>(MyReportModalEffect.Hidden)
    val myReportModalEffect: StateFlow<MyReportModalEffect> get() = _myReportModalEffect

    private val _myReportUiEffect = MutableSharedFlow<MyReportUiEffect>()
    val myReportUiEffect: SharedFlow<MyReportUiEffect> get() = _myReportUiEffect

    fun moveCursor() {
        val myReportState = myReportListState.value as? MyReportListState.Success ?: return

        if (myReportState.nextCursor != cursor.value) {
            isRefreshing.value = true
            cursor.update {
                myReportState.nextCursor
            }
        }
    }

    fun refresh() {
        isRefreshing.value = true
        if (cursor.value == null) {
            myReportListState.restart()
        } else {
            cursor.value = null
        }
    }

    fun deleteIncident(incident: Incident) {
        viewModelScope.launch {
            incidentRepository.deleteIncident(incident.id)
                .onSuccess {
                    showSnackBar("게시글이 삭제되었습니다")
                    navigateBack()
                }.onFailure {
                    showSnackBar("게시글 삭제에 실패했습니다")
                }
        }
    }

    fun showIncidentsActionMenu(incident: Incident) {
        _myReportModalEffect.update {
            MyReportModalEffect.ShowIncidentsActionMenu(incident)
        }
    }

    fun showDeleteCheckDialog(incident: Incident) {
        _myReportModalEffect.update {
            MyReportModalEffect.ShowDeleteCheckDialog(incident)
        }
    }

    fun dismiss() {
        _myReportModalEffect.update {
            MyReportModalEffect.Hidden
        }
    }

    private fun showSnackBar(message: String) {
        viewModelScope.launch {
            _myReportUiEffect.emit(MyReportUiEffect.ShowSnackBar(message))
        }
    }

    fun navigateToIncidentEdit(incident: Incident) {
        viewModelScope.launch {
            _myReportUiEffect.emit(MyReportUiEffect.NavigateToIncidentEdit(incident))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _myReportUiEffect.emit(MyReportUiEffect.NavigateBack)
        }
    }
}