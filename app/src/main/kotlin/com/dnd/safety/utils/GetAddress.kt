package com.dnd.safety.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

fun getAddress(location: LatLng?, context: Context): String {
    if (location == null) {
        return ""
    }

    val geocoder = Geocoder(context, Locale.getDefault())
    var address = ""

    try {
        val geoAddress: Address? = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )?.get(0)

        geoAddress?.let {
            address = changeAddrtoString(geoAddress)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Logger.e("현재 주소를 가져올 수 없음: ${e.message}")
    }

    return address
}

private fun changeAddrtoString(addr: Address): String {
    var address = ""

    try {
        if (addr.getAddressLine(0).isNullOrBlank()) {
            address = if (!addr.adminArea.isNullOrBlank()) {
                addr.adminArea
            } else {
                ""
            } + if (!addr.locality.isNullOrBlank() && addr.adminArea != addr.locality) {
                " " + addr.locality
            } else {
                ""
            } + if (!addr.thoroughfare.isNullOrBlank()) {
                " " + addr.thoroughfare
            } else {
                ""
            } + if (!addr.featureName.isNullOrBlank()) {
                " " + addr.featureName
            } else {
                ""
            }
        } else {
            address = addr.getAddressLine(0).replace(addr.countryName, "").trim()
        }
    } catch (e: Exception) {
        Logger.e("주소 변환 실패: ${e.message}")
    }

    return address
}