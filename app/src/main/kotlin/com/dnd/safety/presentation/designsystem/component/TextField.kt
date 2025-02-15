package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Shape
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
    shape: Shape = SafetyTheme.shapes.r100,
    textStyle: TextStyle = SafetyTheme.typography.paragraph2,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    button: @Composable BoxScope.() -> Unit = {}
) {
    BasicTextField(
        value = value,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        singleLine = true,
        readOnly = readOnly,
        modifier = modifier
    ) { innerTextField ->
        Surface(
            shape = shape,
            color = Gray10,
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
                            text = hint,
                            style = textStyle,
                            color = Gray50
                        )
                    }
                    innerTextField()
                    button()
                }
            }
        }
    }
}

@Composable
fun TextFieldBox(
    text: String,
    shape: Shape,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = SafetyTheme.typography.paragraph2,
    content: @Composable () -> Unit
) {
    Surface(
        shape = shape,
        color = Gray10,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .wrapContentSize(Alignment.TopStart)
        ) {
            Text(
                text = text,
                style = textStyle
            )
            Spacer(modifier = Modifier.weight(1f))
            content()
        }
    }
}