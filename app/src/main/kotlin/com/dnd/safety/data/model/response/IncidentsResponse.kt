package com.dnd.safety.data.model.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class IncidentsResponse(
    val incidents: List<IncidentDto>
) {

    @Serializable
    data class IncidentDto(
        val id: Long,
        @SerializedName("writer_id") val writerId: Long,
        val title: String,
        val description: String,
        @SerializedName("point_x") val pointX: Double,
        @SerializedName("point_Y") val pointY: Double,
        @SerializedName("created_at") val createdDate: String,
        @SerializedName("updated_at") val updatedDate: String,
    )
}
