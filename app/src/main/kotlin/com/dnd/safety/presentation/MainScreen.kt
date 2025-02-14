package com.dnd.safety.presentation

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
import androidx.navigation.compose.NavHost
import com.dnd.safety.presentation.navigation.MainBottomNavItem
import com.dnd.safety.presentation.navigation.component.MainBottomBar
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.ui.home.navigation.homeNavGraph
import com.dnd.safety.presentation.ui.location_search.navigation.locationNavGraph
import com.dnd.safety.presentation.ui.login.navigation.loginNavGraph
import com.dnd.safety.presentation.ui.mytown.navigation.myTownNavGraph
import com.dnd.safety.presentation.ui.nicknameform.navigation.nickNameNavGraph
import com.dnd.safety.presentation.ui.postreport.navigation.postReportNavGraph
import com.dnd.safety.presentation.ui.splash.navigation.splashNavGraph

@Composable
fun MainScreen(
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    onShowSnackBar: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                bottomItems = MainBottomNavItem.entries,
                currentItem = navigator.currentItem,
                onBottomItemClicked = navigator::navigateTo
            )
        },
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
            onShowSnackBar = onShowSnackBar,
        )
    }
}

@Composable
internal fun MainNavHost(
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
            startDestination = navigator.startDestination,
        ) {
            splashNavGraph(
                onPermissionAllowed = navigator::navigateToLogin,
            )
            homeNavGraph(

            )
            loginNavGraph(
                onShowNickName = navigator::navigateToNickNameForm,
                onShowSnackBar = onShowSnackBar
            )
            nickNameNavGraph(
                onShowSearchLocation = navigator::navigateToSearchLocation
            )
            locationNavGraph(
                onShowNavigationConfirm = navigator::navigateToLocationConfirm
            )
            postReportNavGraph(
                onGoBack = navigator::popBackStackIfNotHome
            )
            myTownNavGraph(

            )
        }
    }
}