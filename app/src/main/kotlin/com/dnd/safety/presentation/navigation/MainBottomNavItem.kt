package com.dnd.safety.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainBottomNavItem(
    val icon: ImageVector,
    val route: MainTabRoute,
) {
    Home(
        icon = Icons.Default.Home,
        route = MainTabRoute.Home,
    ),
    Add(
        icon = Icons.Default.CameraEnhance,
        route = MainTabRoute.Add,
    ),
    MyPage(
        icon = Icons.Default.Person,
        route = MainTabRoute.MyPage,
    ),
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