package com.dnd.safety.presentation.ui.myPage

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.R
import com.dnd.safety.domain.model.Incident
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

@Composable
fun MyReportRoute(
    onGoBack: () -> Unit,
    viewModel: MyReportViewModel = hiltViewModel()
) {
    val myReportListState by viewModel.myReportListState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    MyReportScreen(
        myReportListState = myReportListState,
        listState = listState,
        isRefreshing = viewModel.isRefreshing.value,
        onRefresh = viewModel::refresh,
        onGoBack = onGoBack
    )

    LaunchedEffect(listState.canScrollForward) {
        if (!listState.canScrollForward && !viewModel.isRefreshing.value) {
            viewModel.moveCursor()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReportScreen(
    myReportListState: MyReportListState,
    listState: LazyListState,
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
                    listState = listState,
                    myReportListState = myReportListState
                )
            }

        }
    }
}

@Composable
private fun MyReportListContent(
    myReportListState: MyReportListState,
    listState: LazyListState,
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
                    listState = listState,
                    myReports = state.myReports
                )
            }
        }
    }
}

@Composable
private fun MyReportList(
    listState: LazyListState,
    myReports: List<Incident>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
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
    myReport: Incident,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 20.dp, vertical = 17.dp)
    ) {
        CoilImage(
            imageModel = { myReport.firstImage },
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
                text = myReport.description,
                style = SafetyTheme.typography.paragraph2,
                color = Gray50,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.fillMaxWidth()
            )
            Text(
                text = myReport.createdDate.formatLocalDateDot(),
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
            listState = rememberLazyListState(),
            isRefreshing = false,
            onRefresh = { },
            onGoBack = { },
            myReportListState = MyReportListState.Success(
                Incident.sampleIncidents,
                0L
            )
        )
    }
}