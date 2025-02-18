package com.dnd.safety.presentation.ui.login.component
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.data.model.Response
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator
import com.dnd.safety.utils.Logger

@Composable
fun GoogleSignIn(
    launch: (String) -> Unit
) {
    when (val signInWithGoogleResponse = DataProvider.googleSignInResponse) {
        is Response.Loading -> {
            Log.i("Login:GoogleSignIn", "Loading")
            ProgressIndicator()
        }
        is Response.Success -> signInWithGoogleResponse.data?.let { authResult ->
            val token = authResult.user?.getIdToken(true)?.result?.token
            Logger.i("Login:GoogleSignIn Success: $token")
            token?.let { launch(it) }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            if (signInWithGoogleResponse.e.message == "User has already been linked to the given provider.") {
            } else {
                Log.e("Login:GoogleSignIn", "${signInWithGoogleResponse.e}")
            }
        }
    }
}

