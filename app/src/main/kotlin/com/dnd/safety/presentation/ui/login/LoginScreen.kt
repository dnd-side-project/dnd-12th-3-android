package com.dnd.safety.presentation.ui.login

import android.app.Activity
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.dnd.safety.BuildConfig
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator
import com.dnd.safety.presentation.designsystem.component.TextField
import com.dnd.safety.presentation.designsystem.component.WatchOutButton
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun LoginScreen(
    onShowLocationSearch: () -> Unit,
    onShowHome: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val gso = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
        .requestId()
        .requestProfile()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                    viewModel.loginByGoogle(task.result.idToken!!)
                }
            } else {
                onShowSnackBar("${result.resultCode}")
            }
        }

    LoginScreen(
        name = name,
        email = email,
        isLoading = isLoading,
        state = state,
        onKakaoLoginClick = viewModel::loginWithKakao,
        onGoogleLoginClick = { startForResult.launch(googleSignInClient.signInIntent) },
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onClick = viewModel::sendToken
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
private fun LoginScreen(
    name: String,
    email: String,
    isLoading: Boolean,
    state: LoginUiState,
    onKakaoLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(70.dp))
                    WatchOutImage()
                    Spacer(modifier = Modifier.height(50.dp))

                    Crossfade(state) {
                        when (it) {
                            LoginUiState.Initializing ->{}
                            LoginUiState.NeedLogin -> {
                                Column {
                                    Text(
                                        text = "이름",
                                        style = SafetyTheme.typography.label1,
                                        color = Gray50,
                                        modifier = Modifier.padding(horizontal = 30.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    TextField(
                                        value = name,
                                        onValueChange = onNameChange,
                                        hint = "이름을 입력해주세요",
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp),
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = "이메일",
                                        style = SafetyTheme.typography.label1,
                                        color = Gray50,
                                        modifier = Modifier.padding(horizontal = 30.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    TextField(
                                        value = email,
                                        onValueChange = onEmailChange,
                                        hint = "이메일을 입력해주세요",
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp),
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))
                                    WatchOutButton(
                                        text = "회원가입",
                                        onClick = onClick,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                    )
                                }
                            }
                        }
                    }
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
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Image(
            painter = painterResource(R.drawable.login),
            contentDescription = "앱 스플래쉬 이미지",
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.ic_splash_watch_out),
            contentDescription = "앱 스플래쉬 이미지",
            modifier = modifier.width(200.dp)
        )
    }
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
            name = "",
            email = "",
            isLoading = false,
            state = LoginUiState.NeedLogin,
            onKakaoLoginClick = {},
            onGoogleLoginClick = {},
            onNameChange = {},
            onEmailChange = {},
            onClick = {}
        )
    }
}

enum class SocialLoginType {
    KAKAO, GOOGLE
}
