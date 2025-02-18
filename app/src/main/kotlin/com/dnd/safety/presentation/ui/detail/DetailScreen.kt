package com.dnd.safety.presentation.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.R
import com.dnd.safety.domain.model.Comment
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.designsystem.component.FadeAnimatedVisibility
import com.dnd.safety.presentation.designsystem.component.TopAppbar
import com.dnd.safety.presentation.designsystem.component.TopAppbarIcon
import com.dnd.safety.presentation.designsystem.component.TopAppbarType
import com.dnd.safety.presentation.designsystem.component.circleBackground
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.Main
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.presentation.ui.detail.effect.DetailModalEffect
import com.dnd.safety.presentation.ui.home.component.IncidentsItem
import com.dnd.safety.utils.daysAgo
import java.time.LocalDateTime

@Composable
fun DetailRoute(
    incident: Incident,
    onGoBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val commentState by viewModel.commentState.collectAsStateWithLifecycle()

    DetailScreen(
        incident = incident,
        comments = commentState,
        onGoBack = onGoBack,
        onSendComment = viewModel::writeComment,
        onShowCommentActionMenu = viewModel::showCommentActionMenu

    )
}

@Composable
private fun DetailScreen(
    incident: Incident,
    comments: List<Comment>,
    onGoBack: () -> Unit,
    onSendComment: (String) -> Unit,
    onShowCommentActionMenu: (Comment) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                onBackEvent = onGoBack,
                navigationType = TopAppbarType.Custom {
                    Row {
                        TopAppbarIcon(
                            icon = painterResource(id = R.drawable.ic_share),
                            tint = Gray80,
                            onClick = {},
                        )
                        TopAppbarIcon(
                            icon = Icons.Default.MoreVert,
                            tint = Gray80,
                            onClick = {},
                        )
                    }
                }
            )
        },
        containerColor = White,
        modifier = Modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            IncidentsItem(
                incident = incident,
                onLike = {},
            )
            HorizontalDivider(thickness = 8.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(comments) { index, comment ->
                    CommentItem(
                        comment = comment,
                        onShowCommentActionMenu = { onShowCommentActionMenu(comment) }
                    )

                    if (index != comments.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
            }
            CommentTextField(
                name = "John Doe",
                onSendComment = onSendComment,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun CommentTextField(
    name: String,
    onSendComment: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var value by remember { mutableStateOf("") }

    BasicTextField(
        value = value,
        textStyle = SafetyTheme.typography.paragraph2,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                onSendComment(value)
                value = ""
            }
        ),
        onValueChange = { value = it },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        modifier = modifier
    ) { innerTextField ->
        Surface(
            shape = SafetyTheme.shapes.r100,
            border = BorderStroke(1.dp, Gray10),
            color = White,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .wrapContentSize(Alignment.TopStart)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "${name}에게 댓글을 남겨주세요",
                            style = SafetyTheme.typography.paragraph2,
                            color = Gray50
                        )
                    }
                    innerTextField()
                }
                FadeAnimatedVisibility(
                    visible = value.isNotEmpty(),
                ) {
                    if (value.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "menu Icon",
                            tint = White,
                            modifier = Modifier
                                .circleBackground(Main)
                                .padding(4.dp)
                                .clickable {
                                    onSendComment(value)
                                    value = ""
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: Comment,
    onShowCommentActionMenu: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Gray50, shape = RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = comment.writerName,
                style = SafetyTheme.typography.label1
            )
            Text(
                text = comment.date.daysAgo(),
                style = SafetyTheme.typography.label3,
                color = Gray40
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = comment.comment,
                style = SafetyTheme.typography.label5
            )
        }
        if (comment.isMyComment) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "menu Icon",
                tint = Gray40,
                modifier = Modifier
                    .clickable(onClick = onShowCommentActionMenu)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun DetailModalEffect(
    effect: DetailModalEffect,
    viewModel: DetailViewModel
) {
    when (effect) {
        is DetailModalEffect.Hidden -> {}
        is DetailModalEffect.ShowCommentActionMenu -> {
            // Show comment action menu
        }
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    SafetyTheme {
        DetailScreen(
            incident = Incident.sampleIncidents.first(),
            comments = listOf(
                Comment(
                    id = 0,
                    writerId = 0,
                    writerName = "John Doe",
                    date = LocalDateTime.now(),
                    comment = "This is a comment"
                ),
                Comment(
                    id = 0,
                    writerId = 0,
                    writerName = "John Doe",
                    date = LocalDateTime.now(),
                    comment = "This is a comment",
                    isMyComment = true
                )
            ),
            onGoBack = {},
            onSendComment = {},
            onShowCommentActionMenu = {}
        )
    }
}