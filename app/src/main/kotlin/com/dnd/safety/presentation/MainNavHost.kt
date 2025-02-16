package com.dnd.safety.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.dnd.safety.presentation.navigation.MainTab
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.ui.home.navigation.homeNavGraph
import com.dnd.safety.presentation.ui.location_search.navigation.locationNavGraph
import com.dnd.safety.presentation.ui.login.navigation.loginNavGraph
import com.dnd.safety.presentation.ui.my_page.navigation.myPageNavGraph
import com.dnd.safety.presentation.ui.mytown.navigation.myTownNavGraph
import com.dnd.safety.presentation.ui.postreport.navigation.postReportNavGraph
import com.dnd.safety.presentation.ui.splash.navigation.splashNavGraph

@Composable
fun MainNavHost(
    paddingValues: PaddingValues,
    navigator: MainNavigator,
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(paddingValues)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = Route.Splash,
        ) {
            homeNavGraph(
                onBottomNavClicked = navigator::navigateTo
            )
            postReportNavGraph(
                onShowSnackBar = onShowSnackBar,
                onGoBack = navigator::popBackStackIfNotHome,
                onGoBackToHome = { navigator.navigateTo(MainTab.Home) },
                onShowPostReport = navigator::navigateToReport
            )
            myPageNavGraph(
                onGoBack = navigator::popBackStackIfNotHome
            )
            loginNavGraph(
                onShowHome = { navigator.navigateTo(MainTab.Home) },
                onShowLocationSearch = navigator::navigateToSearchLocation,
                onShowSnackBar = onShowSnackBar
            )
            locationNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onComplete = { navigator.navigateTo(MainTab.Home) },
                onShowNavigationConfirm = navigator::navigateToLocationConfirm
            )
            myTownNavGraph(

            )
            splashNavGraph(
                onPermissionAllowed = navigator::navigateToLogin
            )
        }
    }
}