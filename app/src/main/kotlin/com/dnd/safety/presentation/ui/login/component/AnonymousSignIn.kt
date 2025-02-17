package com.dnd.safety.presentation.ui.login.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dnd.safety.data.model.DataProvider
import com.dnd.safety.data.model.Response
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator

@Composable
fun AnonymousSignIn() {
    when (val anonymousResponse = DataProvider.anonymousSignInResponse) {
        is Response.Loading -> {
            Log.i("Login:AnonymousSignIn", "Loading")
            ProgressIndicator()
        }
        is Response.Success -> anonymousResponse.data?.let { authResult ->
            Log.i("Login:AnonymousSignIn", "Success: $authResult")
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.e("Login:AnonymousSignIn", "${anonymousResponse.e}")
        }
    }
}