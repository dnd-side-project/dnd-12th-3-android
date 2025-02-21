package com.dnd.safety.presentation.designsystem.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

data class Action(
    val text: String,
    val onClick: () -> Unit,
)

class ActionBuilder {
    private val actions = mutableListOf<Action>()

    fun action(text: String, onClick: () -> Unit): ActionBuilder {
        actions.add(Action(text = text, onClick = onClick))
        return this
    }

    fun build(): List<Action> = actions

    companion object {
        fun build(block: ActionBuilder.() -> Unit): List<Action> {
            return ActionBuilder().apply(block).build()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionMenu(
    actions: List<Action>,
    onDismissRequest: () -> Unit,
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        content = {
            ActionContent(
                actions = actions,
                onDismissRequest = onDismissRequest
            )
        }
    )
}

@Composable
private fun ActionContent(
    actions: List<Action>,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        actions.forEach { action ->
            ActionButton(
                action = action,
                onDismissRequest = onDismissRequest
            )
        }
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

@Composable
private fun ActionButton(
    action: Action,
    onDismissRequest: () -> Unit
) {
    Text(
        text = action.text,
        style = SafetyTheme.typography.paragraph1,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                action.onClick()
            }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun CommentActionMenuPreview() {
    SafetyTheme {
        Box {
            ActionContent(
                actions = listOf(
                    Action(
                        text = "수정",
                        onClick = { /*TODO*/ }
                    ),
                    Action(
                        text = "삭제",
                        onClick = { /*TODO*/ }
                    ),
                ),
                onDismissRequest = {}
            )
        }
    }
}