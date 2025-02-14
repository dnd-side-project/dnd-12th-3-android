package com.dnd.safety.di

import com.dnd.safety.utils.Logger
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

class HttpNetworkLogger : Interceptor {
    companion object {
        private const val MAX_LOG_LENGTH = 3000 // 최대 500자까지만 로깅
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val logMessage = StringBuilder()
        logMessage.appendLine("\nRequest URL: ${request.url}")

        // 요청 바디 로깅 (길이 제한)
        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            val requestBody = buffer.readUtf8().limitLength()
            logMessage.appendLine("Request Body: $requestBody")
        }

        val startTime = System.nanoTime()
        try {
            val response = chain.proceed(request)
            val endTime = System.nanoTime()
            val durationMs = (endTime - startTime) / 1e6

            logMessage.appendLine("Response URL: ${response.request.url}")
            logMessage.appendLine("Response Code: ${response.code}")
            logMessage.appendLine("Response Time: ${durationMs}ms")

            val responseBody = response.body
            val contentLength = responseBody?.contentLength() ?: 0L

            // 응답 바디 로깅 (길이 제한)
            if (contentLength != 0L) {
                val source = responseBody?.source()
                source?.request(Long.MAX_VALUE)
                val buffer = source?.buffer
                val responseBodyText = buffer?.clone()?.readString(Charsets.UTF_8)?.limitLength()
                logMessage.appendLine("Response Body: $responseBodyText")
            }

            Logger.v(logMessage.toString())
            return response
        } catch (e: Exception) {
            val endTime = System.nanoTime()
            val durationMs = (endTime - startTime) / 1e6
            logMessage.appendLine("Error occurred: ${e.message}")
            logMessage.appendLine("Response Time: ${durationMs}ms")

            Logger.e(logMessage.toString())
            throw e
        }
    }

    // 문자열 길이 제한하는 확장 함수
    private fun String.limitLength(): String {
        return if (this.length > MAX_LOG_LENGTH) {
            this.take(MAX_LOG_LENGTH) + "... (truncated)"
        } else {
            this
        }
    }
}