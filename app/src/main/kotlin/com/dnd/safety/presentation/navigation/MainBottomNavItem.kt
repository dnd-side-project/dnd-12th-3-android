package com.dnd.safety.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainBottomNavItem(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val route: MainTabRoute,
) {
    Home(
        titleRes = 1,
        icon = Icons.Default.Home,
        route = MainTabRoute.Home,
    )
    ;

    companion object {
        fun find(predicate: (MainTabRoute) -> Boolean): MainBottomNavItem? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}