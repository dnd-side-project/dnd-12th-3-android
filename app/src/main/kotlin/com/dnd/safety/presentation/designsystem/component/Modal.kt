package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = Gray80,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null,
        content = content
    )
}

@Composable
fun DefaultDialog(
    title: String = "",
    onDismissRequest: () -> Unit = {},
    shape: Shape = RoundedCornerShape(24.dp),
    properties: DialogProperties = DialogProperties(),
    negativeButton: (@Composable RowScope.() -> Unit)? = null,
    positiveButton: (@Composable RowScope.() -> Unit) = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Dialog(
        properties = properties,
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            shape = shape,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    if (title.isNotEmpty()) {
                        Text(
                            text = title,
                            style = SafetyTheme.typography.body1
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    content()
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    negativeButton?.let { button1 ->
                        button1()
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    positiveButton()
                }
            }
        }
    }
}

@Composable
fun NormalDialog(
    title: String,
    description: String,
    onDismissRequest: () -> Unit,
    positiveText: String = "확인",
    negativeText: String = "취소",
    onPositiveClick: () -> Unit = onDismissRequest,
    onNegativeClick: () -> Unit = onDismissRequest,
) {
    DefaultDialog(
        title = title,
        content = {
            Text(
                text = description,
                style = SafetyTheme.typography.label5
            )
        },
        negativeButton = {
            RegularButton(
                text = negativeText,
                onClick = onNegativeClick,
                isPositive = false,
                modifier = Modifier.weight(1f)
            )
        },
        positiveButton = {
            RegularButton(
                text = positiveText,
                onClick = onPositiveClick,
                modifier = Modifier.weight(1f)
            )
        }
    )
}


@Preview
@Composable
private fun NormalDialogPreview() {
    SafetyTheme {
        NormalDialog(
            title = "Title",
            description = "Description",
            onDismissRequest = {}
        )
    }
}

