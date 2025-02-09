package com.dnd.safety.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.Gray70
import com.dnd.safety.presentation.designsystem.theme.White

@Composable
fun WatchOutTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    isError: Boolean = false,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true
) {
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            val finalValue = maxLength?.let {
                newValue.take(it)
            } ?: newValue
            onValueChange(finalValue)
        },
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isError) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    Gray70
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = if (isError) {
                MaterialTheme.colorScheme.error
            } else {
                White
            },
            textDecoration = null
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        cursorBrush = SolidColor(Color.White),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.padding(0.dp)) {
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                innerTextField()
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun WatchOutTextFieldPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var text by remember { mutableStateOf("") }

        // 기본 상태
        WatchOutTextField(
            value = text,
            onValueChange = { text = it },
            hint = "닉네임을 입력해주세요"
        )

        // 에러 상태
        WatchOutTextField(
            value = "에러 상태",
            onValueChange = {},
            hint = "닉네임을 입력해주세요",
            isError = true
        )
    }
}