package com.dnd.safety.presentation.ui.location_search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.R
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.domain.model.PointDto
import com.dnd.safety.presentation.designsystem.component.TextField
import com.dnd.safety.presentation.designsystem.component.TopAppbar
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.ui.location_search.effect.LocationSearchEffect
import kotlinx.coroutines.delay

@Composable
fun LocationSearchScreen(
    onGoBack: () -> Unit,
    onShowNavigationConfirm: (MyTown) -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: LocationSearchViewModel = hiltViewModel(),
) {
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val lawDistricts by viewModel.lawDistricts.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }

    LocationSearchScreen(
        searchText = searchText,
        lawDistricts = lawDistricts,
        focusRequester = focusRequester,
        onTextChange = viewModel::textChanged,
        onGetPoint = viewModel::getPoint,
        onGoBack = onGoBack
    )

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LocationSearchEffect.NavigateToLocationConfirm -> {
                    onShowNavigationConfirm(effect.myTown)
                }
                is LocationSearchEffect.ShowToast -> onShowSnackBar(effect.message)
            }
        }
    }
}

@Composable
fun LocationSearchScreen(
    searchText: String,
    focusRequester: FocusRequester,
    lawDistricts: List<LawDistrict>,
    onTextChange: (String) -> Unit,
    onGetPoint: (LawDistrict) -> Unit,
    onGoBack: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppbar(
                onBackEvent = onGoBack,
            )
        },
        containerColor = White,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = "주소를 설정해주세요",
                    style = SafetyTheme.typography.title1,
                    modifier = Modifier.padding(horizontal = 4.dp)
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    value = searchText,
                    onValueChange = onTextChange,
                    textStyle = SafetyTheme.typography.paragraph1,
                    hint = "주소 검색",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                        }
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(lawDistricts) { lawDistrict ->
                        LocationSearchItem(
                            lawDistrict = lawDistrict,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onGetPoint(lawDistrict)
                                }
                                .padding(horizontal = 16.dp)
                                .animateItem()
                        )

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationSearchItem(
    lawDistrict: LawDistrict,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Gray50),
                modifier = Modifier
                    .size(34.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(
                    text = lawDistrict.name,
                    style = SafetyTheme.typography.paragraph1,
                )
                Text(
                    text = lawDistrict.address,
                    style = SafetyTheme.typography.label3,
                    color = Gray50
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewLocationSearchScreen() {
    SafetyTheme {
        LocationSearchScreen(
            searchText = "서울",
            lawDistricts = listOf(
                LawDistrict(
                    address = "서울특별시 강남구",
                    roadAddress = "서울특별시 강남구 역삼동 123-45",
                    lotAddress = "서울특별시 강남구 강남대로 123-45",
                    sido = "서울특별시",
                    name = "강남구",
                    pointDto = PointDto(
                        "",
                        "",
                        "",
                        0,
                        0
                    )
                ),
                LawDistrict(
                    address = "서울특별시 강북구",
                    roadAddress = "서울특별시 강남구 역삼동 123-45",
                    lotAddress = "서울특별시 강남구 강남대로 123-45",
                    sido = "서울특별시",
                    name = "강북구",
                    pointDto = PointDto(
                        "",
                        "",
                        "",
                        0,
                        0
                    )
                )
            ),
            focusRequester = FocusRequester(),
            onTextChange = {},
            onGetPoint = {},
            onGoBack = {}

        )
    }
}