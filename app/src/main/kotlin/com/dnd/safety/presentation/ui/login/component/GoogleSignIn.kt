package com.dnd.safety.presentation.ui.login.component
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
            authResult.user?.reload()
                ?.addOnCompleteListener { reloadTask ->
                    if (reloadTask.isSuccessful) {
                        authResult.user?.getIdToken(true)
                            ?.addOnCompleteListener { tokenTask ->
                                if (tokenTask.isSuccessful) {
                                    val idToken = tokenTask.result?.token
                                    Logger.d("Login:GoogleSignIn idToken: $idToken")
                                    launch(idToken ?: "")
                                }
                            }
                    }
                }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.e("Login:GoogleSignIn", "${signInWithGoogleResponse.e}")
        }
    }
}