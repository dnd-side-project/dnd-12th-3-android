package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    textStyle: TextStyle = SafetyTheme.typography.paragraph2,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    BasicTextField(
        value = value,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        singleLine = true,
        modifier = modifier
    ) { innerTextField ->
        Surface(
            shape = SafetyTheme.shapes.r100,
            color = Gray10,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = textStyle,
                            color = Gray50
                        )
                    }
                    innerTextField()
                }
            }
        }
    }
}