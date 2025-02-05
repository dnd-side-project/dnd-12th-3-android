package com.dnd.safety.presentation.navigation

import com.dnd.safety.data.model.Location
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

sealed interface Route {
    val route: String

    @Serializable
    data object Splash : Route {
        override val route: String = "splash"
    }

    @Serializable
    data object Login : Route {
        override val route: String = "login"
    }

    @Serializable
    data object NicknameForm : Route {
        override val route: String = "nicknameform"
    }

    @Serializable
    data class SearchLocation(
        val nickname: String
    ) : Route {
        override val route: String = "search_location/$nickname"

        companion object {
            const val route = "search_location/{nickname}"
            const val argNickname = "nickname"
        }
    }


    @Serializable
    data class LocationConfirm(
        val nickname: String,
        val location: Location
    ) : Route {
        override val route: String = "location_confirm/$nickname/${Json.encodeToString(Location.serializer(), location)}"

        companion object {
            const val route = "location_confirm/{nickname}/{location}"
            const val argNickname = "nickname"
            const val argLocation = "location"
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

