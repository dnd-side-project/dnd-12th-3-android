package com.dnd.safety.presentation.ui.myPage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.repository.MyReportRepository
import com.dnd.safety.presentation.ui.myPage.state.MyReportListState
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReportViewModel @Inject constructor(
    private val myReportRepository: MyReportRepository
) : ViewModel() {

    var isRefreshing = mutableStateOf(false)
        private set

    private val _myReportListState = MutableStateFlow<MyReportListState>(MyReportListState.Loading)
    val myReportListState: StateFlow<MyReportListState> get() = _myReportListState

    private var contentJob: Job? = null

    fun fetchMyReportList() {
        isRefreshing.value = true
        contentJob?.cancel()

        contentJob = viewModelScope.launch {
            myReportRepository
                .getMyReports()
                .onSuccess {
                    _myReportListState.update {
                        MyReportListState.Success(data)
                    }
                    isRefreshing.value = false
                }.onFailure {
                    _myReportListState.update {
                        MyReportListState.Empty
                    }
                    isRefreshing.value = false
                }
        }
    }
}