package com.dnd.safety.presentation.navigation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.dnd.safety.data.model.Location
import com.dnd.safety.presentation.navigation.MainBottomNavItem
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.home.navigation.navigateToHome
import com.dnd.safety.presentation.ui.location_search.navigation.navigateToLocationSearch
import com.dnd.safety.presentation.ui.location_search.navigation.navigationToLocationConfirm
import com.dnd.safety.presentation.ui.login.navigation.navigateToLogin
import com.dnd.safety.presentation.ui.mytown.navigation.navigateToMyTown
import com.dnd.safety.presentation.ui.nicknameform.navigation.navigateToNickName

class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Splash

    val currentItem: MainBottomNavItem?
        get() = MainBottomNavItem.find { tab ->
            navController.currentDestination?.hasRoute(tab::class) == true
        }

    fun navigateTo(menuItem: MainBottomNavItem) {
        if (currentItem == menuItem) return

        val navOptions = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            restoreState = true
        }

        when (menuItem) {
            MainBottomNavItem.Home -> navController.navigateToHome(navOptions)
            MainBottomNavItem.PostReport -> navController.navigate(MainTabRoute.PostReport, navOptions)
            MainBottomNavItem.MyPage -> navController.navigateToMyTown()
        }
    }

    fun navigateToLogin() {
        navController.navigateToLogin()
    }

    fun navigateToNickNameForm() {
        navController.navigateToNickName()
    }

    fun navigateToSearchLocation(nickname: String) {
        navController.navigateToLocationSearch(nickname)
    }

    fun navigateToLocationConfirm(nickname: String, location: Location) {
        navController.navigationToLocationConfirm(nickname, location)
    }

    fun popBackStackIfNotHome(): Boolean {
        return if (isSameCurrentDestination<Route.Splash>() || isSameCurrentDestination<MainTabRoute.Home>()) {
            true
        } else {
            popBackStack()
            false
        }
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }

    @Composable
    fun shouldShowBottomBar() = MainBottomNavItem.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
