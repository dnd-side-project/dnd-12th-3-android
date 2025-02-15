package com.dnd.safety.presentation.ui.search_address_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.PointDto
import com.dnd.safety.domain.model.SearchResult
import com.dnd.safety.presentation.designsystem.component.TextField
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray30
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.Gray60
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White

@Composable
fun SearchAddressDialog(
    onAddressSelected: (SearchResult) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: SearchAddressDialogViewModel = hiltViewModel()
) {
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val lawDistricts by viewModel.lawDistricts.collectAsStateWithLifecycle()

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceDim)
        ) {
            SearchAddressDialogContent(
                searchText = searchText,
                lawDistricts = lawDistricts,
                onDismissRequest = onDismissRequest,
                onTextChange = viewModel::textChanged,
                onGetPoint = viewModel::getPoint,
            )

            if (lawDistricts.isEmpty()) {
                Text(
                    text = "검색 결과가 없습니다.",
                    style = SafetyTheme.typography.label2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .align(Alignment.Center)
                )
            }
        }
    }

    LaunchedEffect(true) {
        viewModel.searchAddressCompleteEffect.collect {
            onAddressSelected(it)
            onDismissRequest()
        }
    }
}

@Composable
private fun SearchAddressDialogContent(
    searchText: String,
    lawDistricts: List<LawDistrict>,
    onDismissRequest: () -> Unit,
    onTextChange: (String) -> Unit,
    onGetPoint: (LawDistrict) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Gray50,
                modifier = Modifier
                    .clickable(onClick = onDismissRequest)
                    .padding(16.dp)
                    .size(25.dp)
            )
            TextField(
                value = searchText,
                onValueChange = onTextChange,
                hint = "장소 검색",
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsIndexed(lawDistricts) { index, lawDistrict ->
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onGetPoint(lawDistrict)
                            }
                            .background(White)
                            .padding(vertical = 11.5.dp, horizontal = 32.dp)
                            .animateItem()
                    ) {
                        Text(
                            text = lawDistrict.name,
                            style = SafetyTheme.typography.paragraph1,
                        )
                        Text(
                            text = lawDistrict.address,
                            style = SafetyTheme.typography.label2,
                            color = Gray60,
                            modifier = Modifier
                        )
                    }
                    if (index != lawDistricts.lastIndex) {
                        HorizontalDivider(
                            color = Gray10,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchAddressDialogPreview() {
    SafetyTheme {
        val sampleDto = PointDto(
            "", "", "", 0, 0
        )
        SearchAddressDialogContent(
            searchText = "서울",
            lawDistricts = listOf(
                LawDistrict(
                    address = "서울특별시 강남구",
                    address2 = "서울특별시 강남구",
                    sido = "서울특별시",
                    name = "강남구",
                    sampleDto
                ),
                LawDistrict(
                    address = "서울특별시 강북구",
                    address2 = "서울특별시 강북구",
                    sido = "서울특별시",
                    name = "강남구",
                    sampleDto
                ),
                LawDistrict(
                    address = "서울특별시 강동구",
                    address2 = "서울특별시 강동구",
                    sido = "서울특별시",
                    name = "강남구",
                    sampleDto
                ),
                LawDistrict(
                    address = "서울특별시 강서구",
                    address2 = "서울특별시 강서구",
                    sido = "서울특별시",
                    name = "강남구",
                    sampleDto
                ),
            ),
            onDismissRequest = {},
            onTextChange = {},
            onGetPoint = {},
        )
    }
}