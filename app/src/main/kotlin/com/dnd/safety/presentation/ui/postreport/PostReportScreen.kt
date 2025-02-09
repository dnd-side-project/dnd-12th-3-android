package com.dnd.safety.presentation.ui.postreport

import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dnd.safety.R
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.presentation.common.components.WatchOutButton
import com.dnd.safety.presentation.designsystem.theme.Gray20
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.Gray70
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.Main
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.Typography
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.navigation.component.MainNavigator
import com.dnd.safety.presentation.ui.photoselection.PhotoSelectionViewModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun PostReportScreen(
    modifier: Modifier = Modifier,
    viewModel: PostReportViewModel = hiltViewModel(),
    navigator: MainNavigator,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentBackStackEntry = navigator.navController.currentBackStackEntry

    LaunchedEffect(currentBackStackEntry) {
        currentBackStackEntry?.savedStateHandle?.getStateFlow<List<Uri>?>(
            PhotoSelectionViewModel.KEY_SELECTED_MEDIA,
            null
        )?.collect { uris ->
            uris?.let { viewModel.updateSelectedMedia(it) }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PostReportEffect.NavigateBack -> navigator.popBackStackIfNotHome()
                is PostReportEffect.ShowToast -> {}
                is PostReportEffect.NavigateToPhotoSelection -> {}
            }
        }
    }

    PostReportContent(
        state = state,
        onImageAdd = viewModel::navigateToPhotoSelection,
        onContentChange = viewModel::updateContent,
        onCategorySelect = viewModel::updateCategory,
        onLocationClick = viewModel::onLocationClick,
        onCompleteClick = viewModel::onCompleteClick
    )
}

@Composable
private fun PostReportContent(
    state: PostReportState,
    onImageAdd: () -> Unit,
    onContentChange: (String) -> Unit,
    onCategorySelect: (IncidentCategory) -> Unit,
    onLocationClick: () -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Gray80)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "사건 사진",
            style = Typography.label1.copy(
                color = Gray20
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Gray70)
                            .clickable { onImageAdd() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_camera),
                                contentDescription = "Add photo",
                                tint = White,
                            )
                            Text(
                                text = "(${state.imageUris.size}/3)",
                                color = White,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.imageUris) { uri ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uri)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "내용",
            style = Typography.label1.copy(
                color = Gray20
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = state.content,
            onValueChange = onContentChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(horizontal = 16.dp)
                .background(
                    color = Gray70,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            textStyle = LocalTextStyle.current.copy(
                color = White
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (state.content.isEmpty()) {
                        Text(
                            text = "어떤 사건을 목격하셨나요?",
                            color = Gray40
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "위치 정보",
            style = Typography.label1.copy(
                color = Gray20
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    color = Gray70,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.location,
                style = MaterialTheme.typography.bodyLarge,
                color = White
            )
            Text(
                text = "변경",
                modifier = Modifier
                    .clickable(onClick = onLocationClick)
                    .padding(8.dp),
                style = TextStyle(textDecoration = TextDecoration.Underline),
                color = Main
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "사건의 종류",
            style = Typography.label1.copy(
                color = Gray20
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
        ) {
            IncidentCategory.getAllExceptAll().forEach { category ->
                FilterChip(
                    selected = state.selectedCategory == category,
                    onClick = { onCategorySelect(category) },
                    label = {
                        Text(
                            text = category.korTitle,
                            style = Typography.label2.copy(
                                color = Gray20,
                            ),
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Gray70,
                        containerColor = Gray70,
                    ),
                    shape = RoundedCornerShape(100.dp),
                    border = if (state.selectedCategory == category) {
                        BorderStroke(1.dp, Gray50)
                    } else {
                        null
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        WatchOutButton(
            text = "완료",
            onClick = onCompleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 27.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostReportScreenPreview() {
    SafetyTheme {
        PostReportContent(
            state = PostReportState(
                content = "",
                selectedCategory = null,
                imageUris = listOf(),
                location = "서울특별시 강남구 역삼동 123-45"
            ),
            onImageAdd = {},
            onContentChange = {},
            onCategorySelect = {},
            onLocationClick = {},
            onCompleteClick = {}
        )
    }
}

// 내용이 있는 상태의 프리뷰
@Preview(showBackground = true)
@Composable
private fun PostReportScreenWithContentPreview() {
    SafetyTheme {
        PostReportContent(
            state = PostReportState(
                content = "사고 내용을 작성한 상태입니다.",
                selectedCategory = IncidentCategory.TRAFFIC,
                imageUris = listOf(),
                location = "서울특별시 강남구 역삼동 123-45"
            ),
            onImageAdd = {},
            onContentChange = {},
            onCategorySelect = {},
            onLocationClick = {},
            onCompleteClick = {}
        )
    }
}