package com.dnd.safety.presentation.ui.login.component


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.data.model.Response
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator
import com.dnd.safety.utils.Logger
import com.google.android.gms.auth.api.identity.BeginSignInResult

@Composable
fun OneTapSignIn(
    launch: (result: BeginSignInResult) -> Unit
) {
    when(val oneTapSignInResponse = DataProvider.oneTapSignInResponse) {
        is Response.Loading ->  {
            Logger.i("Loading")
            ProgressIndicator()
        }
        is Response.Success -> oneTapSignInResponse.data?.let { signInResult ->
            LaunchedEffect(signInResult) {
                launch(signInResult)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Logger.e("${oneTapSignInResponse.e}")
        }
    }
}