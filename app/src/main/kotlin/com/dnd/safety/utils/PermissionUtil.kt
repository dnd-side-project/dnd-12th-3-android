package com.dnd.safety.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

data class Permissions(
    val message: String,
    val manifest: String
) {

    companion object {
        val requiredPermissions = listOf(
            Permissions("위치 정보 접근 권한", Manifest.permission.ACCESS_FINE_LOCATION),
            Permissions("위치 정보 접근 권한", Manifest.permission.ACCESS_COARSE_LOCATION),
            Permissions("카메라 권한", Manifest.permission.CAMERA),
            Permissions("외부 저장소 쓰기 권한", Manifest.permission.WRITE_EXTERNAL_STORAGE),
            Permissions("비디오 파일 접근 권한", Manifest.permission.READ_MEDIA_VIDEO),
            Permissions("이미지 파일 접근 권한", Manifest.permission.READ_MEDIA_IMAGES),
            Permissions("외부 저장소 읽기 권한", Manifest.permission.READ_EXTERNAL_STORAGE),
//            Permissions("알림 권한", Manifest.permission.POST_NOTIFICATIONS),
//            Permissions("통화 기록 읽기 권한", Manifest.permission.READ_CALL_LOG)
        ).filter {
            when (it.manifest) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> Build.VERSION.SDK_INT <= Build.VERSION_CODES.P
                Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.POST_NOTIFICATIONS -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                else -> true
            }
        }

        val androidPermissionList get() = requiredPermissions.map { it.manifest }

        fun findPermissionMessage(permission: String) = requiredPermissions.find { permission.contains(it.manifest) }?.message ?: "접근 권한"
    }
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}