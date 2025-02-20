package com.dnd.safety.presentation.ui.myPage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.repository.MyReportRepository
import com.dnd.safety.presentation.ui.myPage.state.MyReportListState
import com.dnd.safety.utils.trigger.TriggerStateFlow
import com.dnd.safety.utils.trigger.triggerStateIn
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReportViewModel @Inject constructor(
    private val myReportRepository: MyReportRepository
) : ViewModel() {

    var isRefreshing = mutableStateOf(false)
        private set

    var cursor = MutableStateFlow<Long?>(null)

    val myReportListState: TriggerStateFlow<MyReportListState> = cursor.map {
        when (val result = myReportRepository.getMyReports(it)) {
            is ApiResponse.Success -> MyReportListState.Success(result.data.incidents, result.data.nextCursor)
            is ApiResponse.Failure -> MyReportListState.Empty
        }
    }.also {
        isRefreshing.value = false
    }.triggerStateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MyReportListState.Loading
    )

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
        if (cursor.value == 0L) {
            myReportListState.restart()
        } else {
            cursor.value = null
        }
    }
}