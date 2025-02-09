package com.dnd.safety.presentation

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dnd.safety.presentation.navigation.MainBottomNavItem
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.ui.locationconfirm.LocationConfirmScreen
import com.dnd.safety.presentation.ui.locationsearch.LocationSearchScreen
import com.dnd.safety.presentation.ui.login.LoginScreen
import com.dnd.safety.presentation.ui.nicknameform.NicknameFormScreen
import com.dnd.safety.presentation.ui.home.navigation.homeNavGraph
import com.dnd.safety.presentation.ui.splash.SplashRoute
import com.dnd.safety.presentation.ui.splash.navigation.splashNavGraph

@Composable
fun MainScreen(
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    appRestart: () -> Unit,
    onShowSnackBar: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.imePadding()
            )
        }
    ) {
        MainNavHost(
            navigator = navigator,
            paddingValues = it,
            appRestart = appRestart,
            onShowSnackBar = onShowSnackBar,
        )
    }
}

@Composable
internal fun MainNavHost(
    paddingValues: PaddingValues,
    navigator: MainNavigator,
    appRestart: () -> Unit,
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
            startDestination = navigator.startDestination,
        ) {
            splashNavGraph(
                navigator = navigator,
                onPermissionAllowed = { navigator.navigateTo(MainBottomNavItem.Home) },
            )
            homeNavGraph(

            )
            composable(Route.Login.route) {
                LoginScreen(navigator = navigator)
            }

            composable(Route.NicknameForm.route) {
                NicknameFormScreen(navigator = navigator)
            }

            composable(
                route = Route.SearchLocation.route,
                arguments = listOf(
                    navArgument(Route.SearchLocation.argNickname) { type = NavType.StringType }
                )
            ) {
                LocationSearchScreen(navigator = navigator)
            }

            composable(
                route = Route.LocationConfirm.route,
                arguments = listOf(
                    navArgument(Route.LocationConfirm.argNickname) { type = NavType.StringType },
                    navArgument(Route.LocationConfirm.argLocation) { type = NavType.StringType }
                )
            ) {
                LocationConfirmScreen(navigator = navigator)
            }
        }
    }
}