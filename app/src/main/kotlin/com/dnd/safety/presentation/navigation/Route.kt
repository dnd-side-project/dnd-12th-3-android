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
}

sealed interface MainTabRoute : Route {

    @Serializable
    data object Home : MainTabRoute {
        override val route: String
            get() = "home"
    }

}

