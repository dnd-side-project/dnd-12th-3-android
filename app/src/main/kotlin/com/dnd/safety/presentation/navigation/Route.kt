package com.dnd.safety.presentation.navigation

import com.dnd.safety.domain.model.Incident
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object SearchLocation : Route

    @Serializable
    data object FcmMessage : Route

    @Serializable
    data class IncidentDetail(
        val incidentId: Long
    ) : Route

    @Serializable
    data class LocationConfirm(
        val title: String,
        val address: String,
        val pointX: String,
        val pointY: String,
        val sido: String,
        val sgg: String,
        val emd: String,
    ) : Route
}

sealed interface MainTabRoute : Route {

    @Serializable
    data object Home : MainTabRoute

    @Serializable
    sealed interface PostReport : MainTabRoute {

        @Serializable
        data object Camera : PostReport

        @Serializable
        data class Report(val url: String) : PostReport

        @Serializable
        data class Edit(val incident: Incident) : PostReport
    }

    @Serializable
    sealed interface MyPage : MainTabRoute {

        @Serializable
        data object Home : MyPage

        @Serializable
        data object MyReport : MyPage

        @Serializable
        data object MyTown : MyPage

        @Serializable
        data object SingOut : MyPage
    }
}

