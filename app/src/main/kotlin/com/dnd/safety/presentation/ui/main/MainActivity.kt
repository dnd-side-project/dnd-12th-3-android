package com.dnd.safety.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.navigation.component.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            SafetyTheme {
                MainScreen(
                    navigator = navigator
                )
            }
        }
    }
}