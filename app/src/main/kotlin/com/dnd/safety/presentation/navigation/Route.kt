package com.dnd.safety.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object SearchLocation : Route

    @Serializable
    data class LocationConfirm(
        val title: String,
        val address: String,
        val pointX: String,
        val pointY: String
    ) : Route

    @Serializable
    data object PhotoSelection : Route

    @Serializable
    data object MyTown : Route
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
    }

    @Serializable
    data object MyPage : MainTabRoute
}

