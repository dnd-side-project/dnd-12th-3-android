package com.dnd.safety.utils

import android.net.Uri
import okhttp3.MultipartBody

interface FileManager {
    suspend fun getMultipartBody(uri: Uri): MultipartBody.Part
}
