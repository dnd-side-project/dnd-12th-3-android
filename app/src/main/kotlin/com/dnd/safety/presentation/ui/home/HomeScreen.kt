package com.dnd.safety.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {

}

@Composable
fun HomeScreen() {
    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
        ) {

        }
    }
}
