package com.dnd.safety.presentation.ui.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.component.WatchOutLoadingIndicator
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.navigation.utils.GoogleSignInHelper

@Composable
fun LoginScreen(
    onShowNickName: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val signInHelper = remember { GoogleSignInHelper(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val signInResult = signInHelper.getSignInResult(intent)
                signInResult.data?.let { data ->
                    viewModel.onLoginEvent(LoginEvent.GoogleSignInSuccess(data.idToken))
                } ?: run {
                    viewModel.onLoginEvent(
                        LoginEvent.GoogleSignInError(
                            Exception(signInResult.errorMessage ?: "로그인 실패")
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.ShowSnackBar -> onShowSnackBar(effect.message)
                is LoginEffect.NavigateToNickName -> onShowNickName()
            }
        }
    }

    LoginContent(
        state = state,
        onKakaoLoginClick = viewModel::loginWithKakao,
        onGoogleLoginClick = {
            try {
                signInHelper.getSignInIntent()
                    .addOnSuccessListener { result ->
                        launcher.launch(
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        )
                    }
                    .addOnFailureListener { e ->
                        viewModel.onLoginEvent(LoginEvent.GoogleSignInError(e))
                    }
            } catch (e: Exception) {
                viewModel.onLoginEvent(LoginEvent.GoogleSignInError(e))
            }
        }
    )
}

@Composable
fun LoginContent(
    state: LoginState,
    modifier: Modifier = Modifier,
    onKakaoLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    WatchOutImage()

                    Spacer(modifier = Modifier.weight(1f))

                    SocialLoginButton(
                        type = SocialLoginType.KAKAO,
                        onClick = onKakaoLoginClick,
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SocialLoginButton(
                        type = SocialLoginType.GOOGLE,
                        onClick = onGoogleLoginClick,
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.height(48.dp))
                }
                WatchOutLoadingIndicator(state.isLoading)
            }

        }
    }
}

@Composable
fun WatchOutImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_splash_watch_out),
        contentDescription = "앱 스플래쉬 이미지",
        modifier = modifier.size(200.dp)
    )
}


@Composable
fun SocialLoginButton(
    type: SocialLoginType,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Image(
            painter = painterResource(
                id = when (type) {
                    SocialLoginType.KAKAO -> R.drawable.ic_kakao_login
                    SocialLoginType.GOOGLE -> R.drawable.ic_google_login
                }
            ),
            contentDescription = "${type.name} 로그인",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    SafetyTheme {
        LoginContent(
            state = LoginState(isLoading = false),
            onKakaoLoginClick = {},
            onGoogleLoginClick = {},
        )
    }
}

enum class SocialLoginType {
    KAKAO, GOOGLE
}
