package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.response.MyTownResponse
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.domain.model.Point

fun MyTownResponse.toMyTowns() = data.map {
    MyTown(
        id = it.addressId,
        title = null,
        address = it.addressName,
        point = Point(it.longitude, it.latitude),
        selected = false
    )
}