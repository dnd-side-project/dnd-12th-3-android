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
import com.dnd.safety.presentation.navigation.component.MainNavigator

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

        }
    }
}