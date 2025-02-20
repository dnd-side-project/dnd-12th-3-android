package com.dnd.safety.presentation.navigation.component

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.presentation.navigation.MainTab
import com.dnd.safety.presentation.navigation.MainTabRoute
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.ui.detail.navigation.navigateToDetail
import com.dnd.safety.presentation.ui.home.navigation.navigateToHome
import com.dnd.safety.presentation.ui.location_search.navigation.navigateToLocationSearch
import com.dnd.safety.presentation.ui.location_search.navigation.navigationToLocationConfirm
import com.dnd.safety.presentation.ui.login.navigation.navigateToLogin
import com.dnd.safety.presentation.ui.myPage.navigation.navigateToMyPage
import com.dnd.safety.presentation.ui.myPage.navigation.navigateToMyReport
import com.dnd.safety.presentation.ui.myPage.navigation.navigateToMyTown
import com.dnd.safety.presentation.ui.myPage.navigation.navigateToSignOut
import com.dnd.safety.presentation.ui.postreport.navigation.navigateToPostReport
import com.dnd.safety.presentation.ui.postreport.navigation.navigateToPostReportCamera

class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentItem: MainTab?
        @Composable get() = MainTab.find { tab ->
            navController.currentDestination?.hasRoute(tab::class) == true
        }

    fun navigateTo(menuItem: MainTab) {
        when (menuItem) {
            MainTab.Home -> navController.navigateToHome()
            MainTab.PostReport -> navController.navigateToPostReportCamera()
            MainTab.MyPage -> navController.navigateToMyPage()
        }
    }

    fun navigateToLogin() {
        navController.navigateToLogin()
    }

    fun navigateToReport(uri: String) {
        navController.navigateToPostReport(uri)
    }

    fun navigateToMyTown() {
        navController.navigateToMyTown()
    }

    fun navigateToSignOut() {
        navController.navigateToSignOut()
    }

    fun navigateToMyReport() {
        navController.navigateToMyReport()
    }

    fun navigateToSearchLocation() {
        navController.navigateToLocationSearch()
    }

    fun navigateToLocationConfirm(myTown: MyTown) {
        navController.navigationToLocationConfirm(myTown)
    }

    fun navigateToDetail(incident: Incident) {
        navController.navigateToDetail(incident)
    }

    fun popBackStackIfNotHome(): Boolean {
        return if (isSameCurrentDestination<Route.Splash>() || isSameCurrentDestination<Route.Login>() || isSameCurrentDestination<MainTabRoute.Home>()) {
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

    @SuppressLint("RestrictedApi")
    @Composable
    fun shouldShowBottomBar() = currentItem == MainTab.Home
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
