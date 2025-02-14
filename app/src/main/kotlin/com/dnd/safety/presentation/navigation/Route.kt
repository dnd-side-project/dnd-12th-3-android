package com.dnd.safety.presentation.navigation

import com.dnd.safety.data.model.Location
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object NicknameForm : Route

    @Serializable
    data class SearchLocation(
        val nickname: String
    ) : Route

    @Serializable
    data class LocationConfirm(
        val nickname: String,
        val location: Location
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
        data object Gallery : PostReport
    }

    @Serializable
    data object MyPage : MainTabRoute
}

