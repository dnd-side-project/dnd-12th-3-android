package com.dnd.safety.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    val route: String

    @Serializable
    data object Splash : Route {
        override val route: String
            get() = "splash"
    }

    @Serializable
    data object Login : Route {
        override val route: String
            get() = "login"
    }

    @Serializable
    data object NicknameForm : Route {
        override val route: String
            get() = "nicknameForm"
    }

    data class SearchLocation(
        val nickname: String
    ) : Route {
        override val route: String = "search_location/$nickname"

        companion object {
            const val route = "search_location/{nickname}"
            const val argNickname = "nickname"
        }
    }
}

sealed interface MainTabRoute : Route {

    @Serializable
    data object Home : MainTabRoute {
        override val route: String
            get() = "home"
    }

}

