package com.dnd.safety.presentation.ui.postreport

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dnd.safety.R
import com.dnd.safety.data.model.Location
import com.dnd.safety.domain.model.IncidentCategory
import com.dnd.safety.presentation.designsystem.component.FilterButton
import com.dnd.safety.presentation.designsystem.component.TextField
import com.dnd.safety.presentation.designsystem.component.TextFieldBox
import com.dnd.safety.presentation.designsystem.component.WatchOutButton
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.Gray60
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.ui.postreport.effect.PostReportEffect
import com.dnd.safety.presentation.ui.postreport.effect.PostReportModalEffect
import com.dnd.safety.presentation.ui.postreport.photoSelection.PhotoSelectionDialog
import com.dnd.safety.presentation.ui.search_address_dialog.SearchAddressDialog
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun PostReportScreen(
    onGoBackToHome: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: PostReportViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val modalEffect by viewModel.modalEffect.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            viewModel.fetchCurrentLocation()
        }
    }

    LaunchedEffect(Unit) {
        if (hasLocationPermissions(context)) {
            viewModel.fetchCurrentLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PostReportEffect.NavigateBack -> onGoBackToHome()
                is PostReportEffect.ShowToast -> onShowSnackBar(effect.message)
            }
        }
    }

    PostReportContent(
        state = state,
        onImageAdd = viewModel::showPhotoPicker,
        onContentChange = viewModel::updateContent,
        onCategorySelect = viewModel::updateCategory,
        onLocationClick = viewModel::showSearchDialog,
        onCompleteClick = viewModel::onCompleteClick
    )

    PostReportModalContent(
        modalEffect = modalEffect,
        viewModel = viewModel
    )
}

private fun hasLocationPermissions(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
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
    Scaffold(
        containerColor = White,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            ReportTitle(
                title = "사건 사진"
            )
            ReportPicture(
                imageUris = state.imageUris,
                onImageAdd = onImageAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            ReportTitle(
                title = "내용"
            )
            TextField(
                value = state.content,
                onValueChange = onContentChange,
                hint = "어떤 사건을 목격하셨나요?",
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .defaultMinSize(minHeight = 150.dp),
            )
            ReportTitle(
                title = "위치정보"
            )
            TextFieldBox(
                text = state.location.placeName,
                shape = RoundedCornerShape(8.dp),
                onClick =  onLocationClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "변경",
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    color = Gray50,
                    modifier = Modifier
                )
            }
            ReportTitle(
                title = "사건의 종류"
            )
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
            ) {
                IncidentCategory.getAllExceptAll().forEach { category ->
                    FilterButton(
                        isSelected = state.selectedCategory == category,
                        onClick = { onCategorySelect(category) },
                        title = category.korTitle,
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
}

@Composable
private fun ReportPicture(
    imageUris: List<Uri>,
    onImageAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
        item {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Gray10)
                    .clickable(onClick = onImageAdd),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = "Add photo",
                        tint = Gray60,
                    )
                    Text(
                        text = "(${imageUris.size}/3)",
                        color = Gray60,
                        fontSize = 12.sp,
                    )
                }
            }

        }
        items(imageUris) { uri ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = Gray50,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Crop,
            )
        }
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun ReportTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            style = SafetyTheme.typography.label1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun PostReportModalContent(
    modalEffect: PostReportModalEffect,
    viewModel: PostReportViewModel
) {
    when (modalEffect) {
        PostReportModalEffect.Dismiss -> {}
        is PostReportModalEffect.ShowPhotoPickerDialog -> {
            PhotoSelectionDialog(
                onPhotoSelected = viewModel::updateSelectedMedia,
                onDismissRequest = viewModel::dismissModal
            )
        }
        PostReportModalEffect.ShowSearchDialog -> {
            SearchAddressDialog(
                onAddressSelected = viewModel::addressSelected,
                onDismissRequest = viewModel::dismissModal,
            )
        }
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
                location = Location(
                    latitude = 37.501311,
                    longitude = 127.039471,
                    placeName = "강남역",
                    address = "서울특별시 강남구 역삼동 123-45"
                )
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
                location = Location(
                    latitude = 37.501311,
                    longitude = 127.039471,
                    placeName = "강남역",
                    address = "서울특별시 강남구 역삼동 123-45"
                )
            ),
            onImageAdd = {},
            onContentChange = {},
            onCategorySelect = {},
            onLocationClick = {},
            onCompleteClick = {}
        )
    }
}