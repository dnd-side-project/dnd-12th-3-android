package com.dnd.safety.presentation.ui.fcm_message

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.domain.model.FcmMessage
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.designsystem.component.TopAppbar
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.utils.formatLocalDateDot
import com.skydoves.landscapist.coil.CoilImage
import java.time.LocalDateTime

@Composable
fun FcmMessageRoute(
    onGoBack: () -> Unit,
    viewModel: FcmMessageViewModel = hiltViewModel()
) {
    val fcmMessage by viewModel.fcmMessageList.collectAsStateWithLifecycle()

    MyReportScreen(
        fcmMessageList = fcmMessage,
        onGoBack = onGoBack,
    )
}

@Composable
private fun MyReportScreen(
    fcmMessageList: List<FcmMessage>,
    onGoBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            Column {
                TopAppbar(
                    title = "사고 알림",
                    onBackEvent = onGoBack
                )
                HorizontalDivider()
            }
        },
        containerColor = Gray10
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                }
                items(fcmMessageList) { fcmMessage ->
                    FcmMessageItem(
                        fcmMessage = fcmMessage,
                    )
                }
            }
        }
    }
}

@Composable
private fun FcmMessageItem(
    fcmMessage: FcmMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(White)
            .padding(start = 20.dp, end = 12.dp)
            .padding(vertical = 17.dp)
    ) {
        CoilImage(
            imageModel = { fcmMessage.image },
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = modifier.weight(1f)
        ) {
            Text(
                text = fcmMessage.title,
                style = SafetyTheme.typography.paragraph1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.fillMaxWidth()
            )
            Text(
                text = fcmMessage.content,
                style = SafetyTheme.typography.paragraph2,
                color = Gray50,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.fillMaxWidth()
            )
            Text(
                text = fcmMessage.createdAt.formatLocalDateDot(),
                style = SafetyTheme.typography.label2,
                color = Gray50,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun MyReportScreenPreview() {
    SafetyTheme {
        MyReportScreen(
            fcmMessageList = listOf(
                FcmMessage(
                    id = 1,
                    title = "title",
                    content = "content",
                    image = "https://dnd-safety.s3.ap-northeast-2.amazonaws.com/1629780000000",
                    createdAt = LocalDateTime.now()
                )
            ),
            onGoBack = { },
        )
    }
}