package com.dnd.safety.fcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.dnd.safety.R
import com.dnd.safety.domain.repository.FcmRepository
import com.dnd.safety.utils.Deeplink
import com.dnd.safety.utils.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class MyFcmListenerService : FirebaseMessagingService() {

    @Inject
    lateinit var fcmRepository: FcmRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Logger.d("onMessageReceived: ${message.data}")
                val fcmDto = Gson().fromJson(Gson().toJson(message.data), FcmDto::class.java)
                setData(fcmDto)
            } catch (e: Exception) {
                Logger.d("onMessageReceived error :: $e")
            }
        }
    }

    private suspend fun setData(
        fcmDto: FcmDto
    ) {
        insertPush(fcmDto)
        showFcmMessage(fcmDto.title, fcmDto.content, fcmDto.imageUrl)
    }

    private suspend fun insertPush(fcmDto: FcmDto) {
        fcmRepository.insertFcm(fcmDto)
    }

    private suspend fun showFcmMessage(
        title: String,
        content: String,
        imageUrl: String
    ) {
        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.fcm_topic))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker(title)
            .setContentTitle(title)
            .setContentText(content)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val bitmap = getBitmapFromUrl(imageUrl)
        if (bitmap != null) {
            notificationBuilder.setLargeIcon(bitmap)
        }

        withContext(Dispatchers.Main) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(Deeplink.deeplinkWithArgument("type.name")))
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

            val pushId = System.currentTimeMillis().toInt()
            val pendingIntent =
                PendingIntent.getActivity(this@MyFcmListenerService, pushId, intent, flags)
            notificationBuilder.setContentIntent(pendingIntent)

            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.notify(pushId, notificationBuilder.build())

            wakeup()
        }
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            BitmapFactory.decodeStream(url.openStream())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun wakeup() {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
            resources.getString(R.string.fcm_topic)
        )
        wakeLock.acquire(3000)
    }
}