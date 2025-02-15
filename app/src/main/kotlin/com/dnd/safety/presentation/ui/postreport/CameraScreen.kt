package com.dnd.safety.presentation.ui.postreport

import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.White
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CameraScreen(
    onCameraCapture: (String) -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current
    val screeHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    var previewView: PreviewView

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Gray80)
    ) {
        Box(
            modifier = Modifier
                .height(screeHeight * 0.70f)
                .width(screenWidth)
        ) {
            AndroidView(
                factory = {
                    previewView = PreviewView(it)
                    viewModel.showCameraPreview(previewView, lifecycleOwner)
                    previewView
                },
                modifier = Modifier
                    .height(screeHeight * 0.70f)
                    .width(screenWidth)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    viewModel.captureAndSave(context)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shutter),
                    contentDescription = "",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cameraEffect.collectLatest {
            when (it) {
                is CameraEffect.ShowSnackBar -> onShowSnackBar("이미지 저장에 실패했습니다.")
                is CameraEffect.CaptureSuccess -> onCameraCapture(it.uri.toString())
            }
        }
    }
}