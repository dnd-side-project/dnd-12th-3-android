package com.dnd.safety.presentation.ui.main

import android.os.Bundle
import android.provider.Settings.Global
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.dnd.safety.BuildConfig
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.component.NormalDialog
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.navigation.component.rememberMainNavigator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()
            var showAppCloseDialog by remember { mutableStateOf(false) }

            SafetyTheme {
                MainScreen(
                    navigator = navigator
                )

                MainBackHandler(
                    navigator = navigator,
                    onShowAppCloseDialog = { showAppCloseDialog = true }
                )

                if (showAppCloseDialog) {
                    NormalDialog(
                        title = "",
                        description = "앱을 종료하시겠습니까?",
                        onDismissRequest = { showAppCloseDialog = false },
                        onPositiveClick = ::finish
                    )
                }
            }
        }
    }
}

@Composable
private fun MainBackHandler(
    navigator: MainNavigator,
    onShowAppCloseDialog: () -> Unit
) {
    BackHandler {
        val isHome = navigator.popBackStackIfNotHome()
        if (isHome) {
            onShowAppCloseDialog()
        }
    }
}