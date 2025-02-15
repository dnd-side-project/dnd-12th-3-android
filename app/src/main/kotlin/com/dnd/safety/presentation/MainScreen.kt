package com.dnd.safety.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.dnd.safety.presentation.navigation.MainTab
import com.dnd.safety.presentation.navigation.component.MainBottomBar
import com.dnd.safety.presentation.navigation.component.MainNavigator
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navigator: MainNavigator
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val onShowSnackBar: (String) -> Unit = { message ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }

    MainScreenContent(
        navigator = navigator,
        snackBarHostState = snackBarHostState,
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
private fun MainScreenContent(
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    onShowSnackBar: (String) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            MainNavHost(
                navigator = navigator,
                paddingValues = it,
                onShowSnackBar = onShowSnackBar,
            )
        },
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                bottomItems = MainTab.entries.toPersistentList(),
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
    )
}