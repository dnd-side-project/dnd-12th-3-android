package com.dnd.safety.presentation.ui.myPage

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dnd.safety.R
import com.dnd.safety.domain.model.MyReport
import com.dnd.safety.presentation.designsystem.component.ProgressIndicator
import com.dnd.safety.presentation.designsystem.component.PullToRefreshBox
import com.dnd.safety.presentation.designsystem.component.TopAppbar
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.ui.myPage.state.MyReportListState
import com.dnd.safety.utils.formatLocalDateDot
import com.skydoves.landscapist.coil.CoilImage
import java.time.LocalDate

@Composable
fun MyReportRoute(
    onGoBack: () -> Unit,
    viewModel: MyReportViewModel = hiltViewModel()
) {

    MyReportScreen(
        isRefreshing = viewModel.isRefreshing,
        onRefresh = viewModel::refreshMyPort,
        onGoBack = onGoBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReportScreen(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onGoBack: () -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            Column {
                TopAppbar(
                    title = "작성 글 보기",
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
            PullToRefreshBox(
                state = refreshState,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh
            ) {
                MyReportListContent(
                    myReportListState = MyReportListState.Success(
                        listOf(
                            MyReport(
                                id = 1,
                                title = "제목",
                                content = "내용",
                                imageUrl = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                                date = LocalDate.now()
                            ),
                        )
                    )
                )
            }

        }
    }
}

@Composable
private fun MyReportListContent(
    myReportListState: MyReportListState
) {
    Crossfade(myReportListState) { state ->
        when (state) {
            MyReportListState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_empty_list),
                        contentDescription = "Empty List",
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            MyReportListState.Loading -> ProgressIndicator()
            is MyReportListState.Success -> {
                MyReportList(
                    myReports = state.myReports
                )
            }
        }
    }
}

@Composable
private fun MyReportList(
    myReports: List<MyReport>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(White)
            )
        }
        items(myReports) { myReport ->
            MyReportItem(
                myReport = myReport
            )
        }
    }
}

@Composable
private fun MyReportItem(
    myReport: MyReport,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 20.dp, vertical = 17.dp)
    ) {
        CoilImage(
            imageModel = { myReport.imageUrl },
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = myReport.title,
                style = SafetyTheme.typography.paragraph1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.fillMaxWidth()
            )
            Text(
                text = myReport.content,
                style = SafetyTheme.typography.paragraph2,
                color = Gray50,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.fillMaxWidth()
            )
            Text(
                text = myReport.date.formatLocalDateDot(),
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
            isRefreshing = false,
            onRefresh = { },
            onGoBack = { }
        )
    }
}