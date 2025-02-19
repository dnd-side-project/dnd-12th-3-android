package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.Black
import com.dnd.safety.presentation.designsystem.theme.Gray20
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White


@Composable
fun WatchOutButton(
    text: String,
    reverse: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hideKeyboardOnClick: Boolean = false,
) {
    val focusManager = LocalFocusManager.current

    val backgroundColor = if (reverse) {
        MaterialTheme.colorScheme.primary
    } else {
        Gray20
    }

    val contentColor = if (reverse) {
        White
    } else {
        Black
    }

    Button(
        onClick = {
            if (hideKeyboardOnClick) {
                focusManager.clearFocus()
            }
            onClick()
        },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = SafetyTheme.typography.body1B,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WatchOutButtonPreview() {
    SafetyTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 활성화된 버튼
            WatchOutButton(
                text = "다음",
                reverse = true,
                onClick = {}
            )

            // 비활성화된 버튼
            WatchOutButton(
                text = "다음",
                reverse = false,
                onClick = {}
            )
        }
    }
}
