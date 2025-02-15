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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.presentation.designsystem.component.WatchOutButton
import com.dnd.safety.presentation.designsystem.component.WatchOutLoadingIndicator
import com.dnd.safety.presentation.designsystem.component.WatchOutTextField
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.Typography
import com.dnd.safety.presentation.designsystem.theme.White

@Composable
fun NicknameFormScreen(
    onShowSearchLocation: (String) -> Unit,
    viewModel: NicknameFormViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NicknameFormEffect.NavigationToMain -> {
//                    onShowSearchLocation(state.text)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
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
                        style = Typography.title1,
                        color = White,
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
            onShowSearchLocation = {},
        )
    }
}
