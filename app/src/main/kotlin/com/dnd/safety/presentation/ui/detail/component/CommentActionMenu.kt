package com.dnd.safety.presentation.ui.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.component.BorderRegularButton
import com.dnd.safety.presentation.designsystem.component.BottomSheet
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentActionMenu(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        content = {
            CommentActionContent(
                onEdit = onEdit,
                onDelete = onDelete,
                onDismissRequest = onDismissRequest
            )
        }
    )
}

@Composable
fun CommentActionContent(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "수정",
            style = SafetyTheme.typography.paragraph1,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onEdit)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        )
        Text(
            text = "삭제",
            style = SafetyTheme.typography.paragraph1,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onDelete)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        BorderRegularButton(
            text = "닫기",
            onClick = onDismissRequest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentActionMenuPreview() {
    SafetyTheme {
        Box {
            CommentActionContent(
                onEdit = {},
                onDelete = {},
                onDismissRequest = {}
            )
        }
    }
}