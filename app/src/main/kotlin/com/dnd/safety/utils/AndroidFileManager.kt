package com.dnd.safety.utils

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class AndroidFileManager @Inject constructor(
    @ApplicationContext private val context: Context
) : FileManager {

    override suspend fun getMultipartBody(uri: Uri): MultipartBody.Part = withContext(Dispatchers.IO) {
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}")
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val mimeType = context.contentResolver.getType(uri) ?: "image/*"
        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        MultipartBody.Part.createFormData("files", file.name, requestBody)
    }
}