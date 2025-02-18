package com.dnd.safety.presentation.ui.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.R
import com.dnd.safety.data.model.AuthState
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.ui.login.component.AnonymousSignIn
import com.dnd.safety.presentation.ui.login.component.GoogleSignIn
import com.dnd.safety.presentation.ui.login.component.OneTapSignIn
import com.dnd.safety.utils.Logger
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginScreen(
    onShowLocationSearch: () -> Unit,
    onShowHome: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val currentUser = viewModel.currentUser.collectAsStateWithLifecycle().value

    LoginScreen(
        isLoading = isLoading,
        state = state,
        onKakaoLoginClick = viewModel::loginWithKakao,
        onGoogleLoginClick = viewModel::oneTapSignIn,
    )

    LoginEffect(
        currentUser = currentUser,
        viewModel = viewModel,
        onShowLocationSearch = onShowLocationSearch,
        onShowHome = onShowHome,
        onShowSnackBar = onShowSnackBar,
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.ShowSnackBar -> onShowSnackBar(effect.message)
                LoginEffect.NavigateToHome -> onShowHome()
                LoginEffect.NavigateToLocation -> onShowLocationSearch()
            }
        }
    }
}

@Composable
private fun LoginEffect(
    currentUser: FirebaseUser?,
    viewModel: LoginViewModel,
    onShowLocationSearch: () -> Unit,
    onShowHome: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    DataProvider.updateAuthState(currentUser)

    LaunchedEffect(Unit) {
        if (DataProvider.authState != AuthState.SignedOut) {
            currentUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        viewModel.deleteAccount(idToken.toString())
                    }
                }
        } else {
            viewModel.signOut()
            viewModel.loginRequired()
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    Logger.d("Login")
                    val credentials =
                        viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                    viewModel.signInWithGoogle(credentials)
                } catch (e: ApiException) {
                    Logger.e("Login One-tap $e")
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Logger.e("OneTapClient Canceled")
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    AnonymousSignIn(
        onChangeLoading = viewModel::changeLoadingState
    )

    OneTapSignIn(launch = ::launch)

    GoogleSignIn(
        launch = viewModel::loginByGoogle
    )
}

@Composable
private fun LoginScreen(
    isLoading: Boolean,
    state: LoginUiState,
    onKakaoLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
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
                    Crossfade(state) {
                        when (it) {
                            LoginUiState.Initializing -> {}
                            LoginUiState.NeedLogin -> {
                                LoginButtons(
                                    isLoading = isLoading,
                                    onKakaoLoginClick = onKakaoLoginClick,
                                    onGoogleLoginClick = onGoogleLoginClick,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(34.dp))
                }
            }

            if (isLoading) {
                ProgressIndicator()
            }
        }
    }
}

@Composable
private fun LoginButtons(
    isLoading: Boolean,
    onKakaoLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
) {
    Column {
        SocialLoginButton(
            isLoading = isLoading,
            type = SocialLoginType.KAKAO,
            onClick = onKakaoLoginClick,
        )
        Spacer(modifier = Modifier.height(8.dp))
        SocialLoginButton(
            isLoading = isLoading,
            type = SocialLoginType.GOOGLE,
            onClick = onGoogleLoginClick,
        )
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
    isLoading: Boolean,
    type: SocialLoginType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = !isLoading,
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
        LoginScreen(
            isLoading = false,
            state = LoginUiState.NeedLogin,
            onKakaoLoginClick = {},
            onGoogleLoginClick = {},
        )
    }
}

enum class SocialLoginType {
    KAKAO, GOOGLE
}

