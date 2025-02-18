package com.dnd.safety.data.mapper

import com.dnd.safety.data.model.Location
import com.dnd.safety.data.model.response.Document

object LocationMapper {
    fun Document.toLocation(): Location {
        return Location(
            latitude = y.toDouble(),
            longitude = x.toDouble(),
            placeName = place_name,
            address = address_name
        )
    }
}
