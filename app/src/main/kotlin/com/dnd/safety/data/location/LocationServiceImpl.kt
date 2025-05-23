package com.dnd.safety.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import com.dnd.safety.utils.Logger
import com.dnd.safety.utils.hasLocationPermission
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationServiceImpl @Inject constructor(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient
) : LocationService {

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun requestLocationUpdates(intervalMillis: Long): Flow<LatLng?> = callbackFlow {
        if (!context.hasLocationPermission()) {
            trySend(null)
            return@callbackFlow
        }

        locationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                Logger.d("Initial Location: ${it.latitude}, ${it.longitude}")
                trySend(LatLng(it.latitude, it.longitude))
            }
        }

        val request = LocationRequest.Builder(0L)
            .setIntervalMillis(intervalMillis)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let {
                    Logger.d("Location: ${it.latitude}, ${it.longitude}")
                    trySend(LatLng(it.latitude, it.longitude))
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }.catch {
        Logger.e("$it")
        emit(null)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LatLng? {
        if (!context.hasLocationPermission()) return null

        return try {
            val location = locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null
            ).await()

            location?.let {
                Logger.d("Current Location: ${it.latitude}, ${it.longitude}")
                LatLng(it.latitude, it.longitude)
            }
        } catch (e: Exception) {
            Logger.e("Failed to get location: $e")
            null
        }
    }
}