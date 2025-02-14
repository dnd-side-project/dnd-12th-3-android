package com.dnd.safety.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.utils.Permissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashRoute(
    onPermissionAllowed: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dialogQueue by viewModel.visiblePermissionDialogQueue.collectAsStateWithLifecycle()
    val multiplePermission = rememberMultiplePermissionsState(
        permissions = Permissions.androidPermissionList,
        onPermissionsResult = viewModel::onPermissionResult
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center,
    ) {
        SplashImage()
    }

    if (multiplePermission.allPermissionsGranted) {
        onPermissionAllowed()
        return
    }

    LaunchedEffect(true) {
        multiplePermission.launchMultiplePermissionRequest()
    }
}

@Composable
private fun SplashImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_splash_watch_out),
        contentDescription = "앱 스플래쉬 이미지",
        modifier = modifier.size(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SafetyTheme {
        SplashRoute(
            onPermissionAllowed = {}
        )
    }
}
