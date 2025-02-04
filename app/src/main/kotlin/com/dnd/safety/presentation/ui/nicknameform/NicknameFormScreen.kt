package com.dnd.safety.presentation.ui.nicknameform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.dnd.safety.presentation.common.components.WatchOutButton
import com.dnd.safety.presentation.common.components.WatchOutLoadingIndicator
import com.dnd.safety.presentation.common.components.WatchOutTextField
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.theme.SafetyTheme

@Composable
fun NicknameFormScreen(
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
    viewModel: NicknameFormViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NicknameFormEffect.NavigationToMain -> {
                    navigator.navigateTo(
                        Route.SearchLocation(state.text),
                        navOptions {
                            popUpTo(Route.Splash.route) {
                                inclusive = true
                            }
                        }
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(    // Box로 감싸서 중첩 레이아웃 구성
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Spacer(modifier = Modifier.height(100.dp))

                    Text(
                        text = "닉네임을 작성해주세요",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    NicknameTextField(
                        value = state.text,
                        onValueChange = { viewModel.updateNickName(it) },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    WatchOutButton(
                        text = "다음",
                        enabled = state.isButtonEnabled,
                        onClick = viewModel::navigateToNext,
                        hideKeyboardOnClick = true,
                    )
                }
            }

            if (state.isLoading) {
                WatchOutLoadingIndicator(isLoading = true)
            }
        }
    }
}

@Composable
private fun NicknameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()

    ) {
        WatchOutTextField(
            value = value,
            onValueChange = onValueChange,
            maxLength = 10,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp, top = 4.dp)
        ) {
            Text(
                text = "${value.length}/10",
                modifier = Modifier.align(Alignment.CenterEnd),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NicknameFormScreenPreview() {
    SafetyTheme {
        NicknameFormScreen(
            navigator = MainNavigator(navController = rememberNavController())
        )
    }
}
