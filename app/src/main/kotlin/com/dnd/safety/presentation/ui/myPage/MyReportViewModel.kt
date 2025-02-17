package com.dnd.safety.presentation.ui.myPage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyReportViewModel @Inject constructor(

) : ViewModel() {

    var isRefreshing by mutableStateOf(false)
        private set

    fun refreshMyPort() {
        isRefreshing = true
    }
}