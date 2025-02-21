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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.domain.model.Comment
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.designsystem.component.Action
import com.dnd.safety.presentation.designsystem.component.ActionBuilder
import com.dnd.safety.presentation.designsystem.component.ActionMenu
import com.dnd.safety.presentation.designsystem.component.FadeAnimatedVisibility
import com.dnd.safety.presentation.designsystem.component.NormalDialog
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
import com.dnd.safety.presentation.ui.detail.effect.DetailUiEffect
import com.dnd.safety.presentation.ui.detail.state.DetailUiState
import com.dnd.safety.presentation.ui.home.component.IncidentsItem
import com.dnd.safety.utils.daysAgo
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime

@Composable
fun DetailRoute(
    onGoBack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onShowIncidentEdit: (Incident) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val commentState by viewModel.commentState.collectAsStateWithLifecycle()
    val modalEffect by viewModel.detailModalState.collectAsStateWithLifecycle()
    val commentMode by viewModel.commentMode.collectAsStateWithLifecycle()
    val incident by viewModel.incident.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    DetailContent(
        commentMode = commentMode,
        commentState = commentState,
        onGoBack = onGoBack,
        detailUiState = incident,
        viewModel = viewModel
    )

    DetailModalEffect(
        effect = modalEffect,
        viewModel = viewModel
    )

    LaunchedEffect(Unit) {
        viewModel.detailUiEffect.collectLatest {
            when (it) {
                DetailUiEffect.HideKeyboard -> keyboardController?.hide()
                is DetailUiEffect.ShowSnackBar -> onShowSnackBar(it.message)
                is DetailUiEffect.NavigateToIncidentEdit -> onShowIncidentEdit(it.incident)
            }

        }
    }
}

@Composable
private fun DetailContent(
    commentMode: CommentMode,
    commentState: List<Comment>,
    onGoBack: () -> Unit,
    detailUiState: DetailUiState,
    viewModel: DetailViewModel
) {
    FadeAnimatedVisibility(
        visible = detailUiState is DetailUiState.IncidentDetail,
    ) {
        if (detailUiState is DetailUiState.IncidentDetail) {
            DetailScreen(
                commentMode = commentMode,
                incident = detailUiState.incident,
                comments = commentState,
                onGoBack = onGoBack,
                onWriteComment = viewModel::writeComment,
                onEditComment = viewModel::editComment,
                onShowCommentActionMenu = viewModel::showCommentActionMenu,
                onShowIncidentsActionMenu = viewModel::showIncidentsActionMenu,
                onCloseEditMode = viewModel::closeCommentEditMode,
                onLike = viewModel::likeIncident
            )
        }
    }
}

@Composable
private fun DetailScreen(
    commentMode: CommentMode,
    incident: Incident,
    comments: List<Comment>,
    onGoBack: () -> Unit,
    onWriteComment: (String) -> Unit,
    onEditComment: (Long, String) -> Unit,
    onShowCommentActionMenu: (Comment) -> Unit,
    onShowIncidentsActionMenu: () -> Unit,
    onCloseEditMode: () -> Unit,
    onLike: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppbar(
                onBackEvent = onGoBack,
                navigationType = TopAppbarType.Custom {
                    Row {
//                        TopAppbarIcon(
//                            icon = painterResource(id = R.drawable.ic_share),
//                            tint = Gray80,
//                            onClick = {},
//                        )
                        if (incident.editable) {
                            TopAppbarIcon(
                                icon = Icons.Default.MoreVert,
                                tint = Gray80,
                                onClick = onShowIncidentsActionMenu,
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            CommentTextMode(
                commentMode = commentMode,
                onSendComment = onWriteComment,
                onEditComment = onEditComment,
                onCloseEditMode = onCloseEditMode
            )
        },
        containerColor = White,
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    IncidentsItem(
                        incident = incident,
                        imageHeight = 200.dp,
                        onLike = onLike,
                    )
                    HorizontalDivider(thickness = 8.dp)
                }
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
        }
    }
}

sealed interface CommentMode {
    data class Text(val name: String) : CommentMode
    data class Edit(val originText: String, val commentId: Long) : CommentMode
}

@Composable
private fun CommentTextMode(
    commentMode: CommentMode,
    onSendComment: (String) -> Unit,
    onEditComment: (Long, String) -> Unit,
    onCloseEditMode: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Gray10)
    ) {
        FadeAnimatedVisibility(
            visible = commentMode is CommentMode.Edit,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "메시지 수정",
                        style = SafetyTheme.typography.paragraph1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                    Text(
                        text = (commentMode as? CommentMode.Edit)?.originText ?: "",
                        style = SafetyTheme.typography.label2,
                        color = Gray50,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "menu Icon",
                    tint = Main,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable(onClick = onCloseEditMode)
                        .padding(4.dp)
                )
            }
        }
        CommentTextField(
            name = (commentMode as? CommentMode.Text)?.name ?: "",
            originText = (commentMode as? CommentMode.Edit)?.originText ?: "",
            onSendComment = {
                when (commentMode) {
                    is CommentMode.Text -> onSendComment(it)
                    is CommentMode.Edit -> onEditComment(commentMode.commentId, it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun CommentTextField(
    name: String,
    originText: String,
    onSendComment: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var value by remember { mutableStateOf("") }

    LaunchedEffect(originText) {
        if (originText.isNotEmpty()) {
            value = originText
        }
    }

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
                    if (value.isEmpty() && name.isNotEmpty()) {
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
            ActionMenu(
                actions = ActionBuilder.build {
                    action(
                        text = "댓글 수정",
                        onClick = {
                            viewModel.showCommentEditMode(effect.comment)
                        }
                    )
                    action(
                        text = "댓글 삭제",
                        onClick = {
                            viewModel.deleteComment(effect.comment.commentId)
                        }
                    )
                },
                onDismissRequest = viewModel::dismiss
            )
        }
        DetailModalEffect.ShowIncidentsActionMenu -> {
            ActionMenu(
                actions = ActionBuilder.build {
                    action(
                        text = "게시글 수정",
                        onClick = viewModel::showIncidentEditScreen
                    )
                    action(
                        text = "게시글 삭제",
                        onClick = viewModel::showDeleteCheckDialog
                    )
                },
                onDismissRequest = viewModel::dismiss
            )
        }
        DetailModalEffect.ShowDeleteCheckDialog -> {
            NormalDialog(
                title = "",
                description = "개시글을 삭제할까요?",
                onDismissRequest = viewModel::dismiss,
                onPositiveClick = viewModel::deleteIncident
            )
        }
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    SafetyTheme {
        DetailScreen(
            commentMode = CommentMode.Edit("This is a comment", 1),
            incident = Incident.sampleIncidents.first(),
            comments = listOf(
                Comment(
                    commentId = 0,
                    writerName = "John Doe",
                    date = LocalDateTime.now(),
                    comment = "This is a comment"
                ),
                Comment(
                    commentId = 0,
                    writerName = "John Doe",
                    date = LocalDateTime.now(),
                    comment = "This is a comment",
                    isMyComment = true
                )
            ),
            onGoBack = {},
            onWriteComment = {},
            onShowCommentActionMenu = {},
            onCloseEditMode = {},
            onEditComment = { _, _ -> },
            onShowIncidentsActionMenu = {},
            onLike = {}
        )
    }
}