package com.dnd.safety.presentation.ui.postreport.photoSelection

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.dnd.safety.domain.model.Media
import com.dnd.safety.domain.model.MediaType
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.Main
import com.dnd.safety.presentation.designsystem.theme.Typography
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.ui.postreport.effect.PhotoSelectionEffect
import com.dnd.safety.presentation.ui.postreport.state.PhotoSelectionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PhotoSelectionDialog(
    onPhotoSelected: (List<Uri>) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: PhotoSelectionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA,
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        viewModel.onPermissionResult(isGranted)
    }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isGranted = requiredPermissions.all {
                    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                }
                viewModel.onPermissionResult(isGranted)
                if (isGranted) {
                    viewModel.loadMedia()
                }
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PhotoSelectionEffect.NavigateBack -> {
                    onPhotoSelected(state.selectedMedia.map { it.uri })
                    onDismissRequest()
                }
                is PhotoSelectionEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                PhotoSelectionEffect.RequestPermission -> {
                    launcher.launch(requiredPermissions)
                }
            }
        }
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest,
    ) {
        PhotoSelectionContent(
            state = state,
            onNavigateBack = onDismissRequest,
            onConfirmClick = viewModel::onConfirmClick,
            onMediaSelected = viewModel::onMediaSelected,
            onCameraClick = {
                val intent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA).apply {
                    val photoFile = createImageFile(context)
                    val photoUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        photoFile
                    )
                    viewModel.setTempPhotoUri(photoUri)
                    putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
                cameraLauncher.launch(intent)
            },
            onRequestPermission = { launcher.launch(requiredPermissions) },
        )
    }
}

private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    )
}

@Composable
private fun PhotoSelectionContent(
    state: PhotoSelectionState,
    onNavigateBack: () -> Unit,
    onConfirmClick: () -> Unit,
    onMediaSelected: (Media) -> Unit,
    onRequestPermission: () -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val mediaItems = state.mediaFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            PhotoSelectionTopBar(
                selectedCount = state.selectedMedia.size,
                onNavigateBack = onNavigateBack,
                onConfirmClick = onConfirmClick,
                isConfirmEnabled = state.selectedMedia.isNotEmpty()
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isPermissionGranted == null || state.isLoading -> ProgressIndicator()
                !state.isPermissionGranted -> PermissionRequest(onRequestPermission)
                else -> MediaGrid(
                    mediaItems = mediaItems,
                    selectedMedia = state.selectedMedia,
                    onMediaSelected = onMediaSelected,
                    onCameraClick = onCameraClick,
                )
            }
        }
    }
}

@Composable
private fun PermissionRequest(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("미디어 접근 권한이 필요합니다")
            Button(onClick = onRequestPermission) {
                Text("권한 허용하기")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoSelectionTopBar(
    selectedCount: Int,
    onNavigateBack: () -> Unit,
    onConfirmClick: () -> Unit,
    isConfirmEnabled: Boolean
) {
    TopAppBar(
        title = {
            Text(
                text = "최근 항목",
                style = Typography.paragraph1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = White
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Gray80
        ),
        actions = {
            TextButton(
                onClick = onConfirmClick,
                enabled = isConfirmEnabled
            ) {
                Text(
                    text = "확인 ($selectedCount/3)",
                    color = if (isConfirmEnabled) Main else Gray40
                )
            }
        }
    )
}

@Composable
private fun MediaGrid(
    mediaItems: LazyPagingItems<Media>,
    selectedMedia: List<Media>,
    onMediaSelected: (Media) -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            CameraItem(
                onCameraClick = onCameraClick,
            )
        }

        items(
            count = mediaItems.itemCount,
            key = mediaItems.itemKey { it.uri.toString() }
        ) { index ->
            val media = mediaItems[index]
            media?.let { mediaItem ->
                MediaGridItem(
                    media = mediaItem,
                    isSelected = selectedMedia.contains(mediaItem),
                    selectionIndex = selectedMedia.indexOf(mediaItem) + 1,
                    onMediaSelected = onMediaSelected
                )
            }
        }
    }
}

@Composable
private fun CameraItem(
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .background(Gray40.copy(alpha = 0.1f))
            .clickable(onClick = onCameraClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.PhotoCamera,
            contentDescription = "카메라",
            modifier = Modifier.size(32.dp),
            tint = Gray40
        )
    }
}

@Composable
private fun MediaGridItem(
    media: Media,
    isSelected: Boolean,
    selectionIndex: Int,
    onMediaSelected: (Media) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clickable { onMediaSelected(media) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(media.uri)
                .apply {
                    if (media.type == MediaType.VIDEO) {
                        decoderFactory(VideoFrameDecoder.Factory())
                    }
                }
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
        )

        if (media.type == MediaType.VIDEO) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "비디오",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(24.dp)
                        .background(Main, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectionIndex.toString(),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}