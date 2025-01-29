package com.dnd.safety.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

}

sealed interface MainTabRoute : Route {

    @Serializable
    data object Home : MainTabRoute


}

