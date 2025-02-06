package com.dnd.safety.presentation.ui.locationsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.dnd.safety.R
import com.dnd.safety.data.model.Location
import com.dnd.safety.presentation.common.components.WatchOutLoadingIndicator
import com.dnd.safety.presentation.common.components.WatchOutTextField
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.Typography
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.navigation.Route
import com.dnd.safety.presentation.navigation.component.MainNavigator
import kotlinx.coroutines.delay

@Composable
fun LocationSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationSearchViewModel = hiltViewModel(),
    navigator: MainNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LocationSearchEffect.NavigateToLocationConfirm -> {
                    navigator.navigateTo(
                        Route.LocationConfirm(
                            nickname = viewModel.nickname,
                            location = effect.location
                        )
                    )
                }
                is LocationSearchEffect.ShowToast -> {}
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray80,
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Gray80,
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "주소를 설정해주세요",
                    style = Typography.title1,
                    color = White,
                )

                Spacer(modifier = Modifier.height(20.dp))

                WatchOutTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            viewModel.searchLocation()
                        }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        WatchOutLoadingIndicator(
                            isLoading = true,
                            backgroundColor = Color.Transparent
                        )
                    }
                } else if (state.locations.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        items(state.locations) { location ->
                            LocationSearchItem(
                                location = location,
                                onClick = { viewModel.onLocationSelect(it) }
                            )
                        }
                    }
                } else if (state.searchQuery.isNotEmpty() && state.hasSearched) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "위치 정보를 찾을 수 없습니다",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationSearchItem(
    location: Location,
    onClick: (Location) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(location) }
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = location.placeName,
                style = Typography.paragraph1,
                color = White
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewLocationSearchScreen() {
    MaterialTheme {
        LocationSearchScreen(
            navigator = MainNavigator(rememberNavController())
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun PreviewLocationSearchItem() {
    SafetyTheme {
        LocationSearchItem(
            location = Location(
                placeName = "서울특별시 강남구",
                address = "서울특별시 강남구 역삼동 123-456",
                latitude = 37.49795,
                longitude = 127.02761,
            ),
            onClick = {}
        )
    }
}