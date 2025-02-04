package com.dnd.safety.presentation.ui.searchlocation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dnd.safety.presentation.common.components.WatchOutTextField
import com.dnd.safety.presentation.navigation.component.MainNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLocationScreen(
    modifier: Modifier = Modifier,
    nickname: String,
//    viewModel: SearchLocationViewModel = hiltViewModel(),
    navigator: MainNavigator
) {
//    val state by viewModel.state.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        viewModel.effect.collect { effect ->
//            when (effect) {
//                // TODO: Effect 처리
//            }
//        }
//    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("위치 설정") },
                navigationIcon = {
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//                WatchOutTextField(
//                    value = state.searchQuery,
//                    onValueChange = viewModel::updateSearchQuery,
//                    hint = "주소를 입력해주세요"
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // 검색 결과 리스트
//                LazyColumn(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    // TODO: 검색 결과 표시
//                }
//
//                if (state.isLoading) {
//                    WatchOutLoadingIndicator(isLoading = true)
//                }
//            }
        }
    }
}