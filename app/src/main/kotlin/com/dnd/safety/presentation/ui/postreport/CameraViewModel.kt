package com.dnd.safety.presentation.ui.postreport

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnd.safety.domain.repository.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraRepository: CameraRepository
) : ViewModel() {

    private val _cameraEffect = MutableSharedFlow<CameraEffect>()
    val cameraEffect: SharedFlow<CameraEffect> get() = _cameraEffect

    fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModelScope.launch {
            cameraRepository.showCameraPreview(
                previewView,
                lifecycleOwner
            )
        }
    }

    fun captureAndSave(context: Context) {
        viewModelScope.launch {
            cameraRepository.captureAndSaveImage(
                context,
                onSuccess = ::captureSuccess,
                onError = ::showSnackBar
            )
        }
    }

    private fun showSnackBar() {
        viewModelScope.launch {
            _cameraEffect.emit(CameraEffect.ShowSnackBar)
        }
    }

    private fun captureSuccess(uri: Uri) {
        viewModelScope.launch {
            _cameraEffect.emit(CameraEffect.CaptureSuccess(uri))
        }
    }
}

sealed interface CameraEffect {
    data object ShowSnackBar : CameraEffect
    data class CaptureSuccess(val uri: Uri) : CameraEffect
}