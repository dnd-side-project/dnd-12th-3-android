package com.dnd.safety.presentation.ui.locationconfirm

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.data.model.Location
import com.dnd.safety.presentation.common.components.WatchOutButton
import com.dnd.safety.presentation.designsystem.theme.Typography
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.navigation.component.rememberMainNavigator
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationConfirmScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationConfirmViewModel = hiltViewModel(),
    navigator: MainNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 위치 권한 요청
    val locationPermissionState = rememberUpdatedState(Manifest.permission.ACCESS_FINE_LOCATION)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.requestLocationPermission(isGranted)
        }
    )
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(locationPermissionState.value)
    }

    // 알림 권한 요청을 위한 런처 설정
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
            viewModel.requestNotificationPermission(areNotificationsEnabled)
        }
    )

    // 알림 권한 요청 함수
    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val areNotificationsEnabled = notificationManager.areNotificationsEnabled()

            if (!areNotificationsEnabled) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                notificationPermissionLauncher.launch(intent)
            } else {
                viewModel.requestNotificationPermission(true)
            }
        } else {
            viewModel.requestNotificationPermission(true)
        }
    }

    // 알림 권한 요청
    LaunchedEffect(Unit) {
        requestNotificationPermission()
    }

    // 화면 전환 효과 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LocationConfirmEffect.NavigateToMainScreen -> {
                    // TODO: 홈 화면 이동 구현
                }
                is LocationConfirmEffect.ShowPermissionDeniedMessage -> {
                    // Toast 메시지 표시 등의 처리
                }
            }
        }
    }

    // 위치 권한이 승인되었을 때
    if (state.isPermissionGranted && state.isNotificationPermissionGranted) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(viewModel.location.latitude, viewModel.location.longitude),
                15f
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                Text(
                    text = "해당 주소로 알림을\n보내드릴까요?",
                    style = Typography.title1,
                    color = White,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                viewModel.location.latitude,
                                viewModel.location.longitude
                            )
                        ),
                        title = viewModel.location.placeName,
                        snippet = viewModel.location.address
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                WatchOutButton(
                    text = "다음",
                    enabled = state.isPermissionGranted && state.isNotificationPermissionGranted,
                    onClick = viewModel::onNextButtonClicked,
                )

                Spacer(modifier = Modifier.height(68.dp))
            }
        }
    } else {
        Text("위치 권한 또는 알림 권한이 필요합니다.")
    }
}