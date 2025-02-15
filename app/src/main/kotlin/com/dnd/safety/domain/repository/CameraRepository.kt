package com.dnd.safety.domain.repository

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import java.lang.Error

interface CameraRepository {

    suspend fun captureAndSaveImage(
        context: Context,
        onSuccess: (Uri) -> Unit,
        onError: () -> Unit
    )

    suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    )
}