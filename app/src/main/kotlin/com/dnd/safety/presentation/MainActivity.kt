package com.dnd.safety.presentation

import android.content.Intent
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.navigation.component.rememberMainNavigator
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()
            val coroutineScope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }
            val onShowSnackBar: (String) -> Unit = { message ->
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(message)
                }
            }

            SafetyTheme {
                MainScreen(
                    navigator = navigator,
                    snackBarHostState = snackBarHostState,
                    appRestart = ::appRestart,
                    onShowSnackBar = onShowSnackBar,
                )
            }
        }
    }

    private fun appRestart() {
        val packageManager = packageManager
        val intent = packageManager?.getLaunchIntentForPackage(packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        startActivity(mainIntent)
        exitProcess(0)
    }
}