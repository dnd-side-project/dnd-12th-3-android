package com.dnd.safety.presentation.ui.sample

import androidx.lifecycle.ViewModel
import com.dnd.safety.domain.repository.SampleRepository
import com.dnd.safety.presentation.ui.sample.effect.SampleEffect
import com.dnd.safety.presentation.ui.sample.state.SampleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val repository: SampleRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(SampleState())
    val uiState = _uiState.asStateFlow()

    private val _dialogEffect = MutableStateFlow<SampleEffect>(SampleEffect.Idle)
    val dialogEffect = _dialogEffect.asStateFlow()
}