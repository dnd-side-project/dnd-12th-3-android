package com.dnd.safety.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.ui.splash.effect.SplashNavigationEffect

@Composable
fun SplashScreen(
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
) {
    SplashNavigationEffect(navigator)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Gray80),
        contentAlignment = Alignment.Center,
    ) {
        SplashImage()
    }
}

@Composable
private fun SplashImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_splash_watch_out),
        contentDescription = "앱 스플래쉬 이미지",
        modifier = modifier.size(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SafetyTheme {
        val fakeNavController = rememberNavController()
        val fakeNavigator = remember {
            MainNavigator(fakeNavController)
        }

        SplashScreen(navigator = fakeNavigator)
    }
}