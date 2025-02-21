package com.dnd.safety.presentation.ui.fcm_message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.model.FcmMessage
import com.dnd.safety.domain.repository.FcmRepository
import com.dnd.safety.presentation.ui.myPage.effect.MyReportUiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FcmMessageViewModel @Inject constructor(
    fcmRepository: FcmRepository
) : ViewModel() {

    val fcmMessageList: StateFlow<List<FcmMessage>> = fcmRepository.getFcmFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = emptyList()
        )

}