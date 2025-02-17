package com.dnd.safety.presentation.ui.login.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.data.model.Response
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator

@Composable
fun AnonymousSignIn(
    onChangeLoading: (Boolean) -> Unit
) {
    when (val anonymousResponse = DataProvider.anonymousSignInResponse) {
        is Response.Loading -> {
            onChangeLoading(true)
        }
        is Response.Success -> anonymousResponse.data?.let { authResult ->
            Log.i("Login:AnonymousSignIn", "Success: $authResult")
            onChangeLoading(false)
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.e("Login:AnonymousSignIn", "${anonymousResponse.e}")
            onChangeLoading(false)
        }
    }
}