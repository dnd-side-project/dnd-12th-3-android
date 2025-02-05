package com.dnd.safety.presentation.ui.locationconfirm

data class LocationConfirmState(
    val isPermissionGranted: Boolean = false,  // 위치 권한 상태
    val isNotificationPermissionGranted: Boolean = false,  // 알림 권한 상태
    val isLoading: Boolean = false,
)
