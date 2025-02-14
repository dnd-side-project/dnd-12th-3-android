package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.Gray20
import com.dnd.safety.presentation.designsystem.theme.Gray70
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun IconButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = SafetyTheme.typography.label3,
    containerColor: Color = Gray70,
    contentColor: Color = Gray20
) {
    SmallButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = style,
        containerColor = containerColor,
        contentColor = contentColor,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .size(15.dp)

            )
        }
    )
}

@Composable
fun SmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = SafetyTheme.typography.label3,
    containerColor: Color = Gray70,
    contentColor: Color = Gray20,
    icon: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(SafetyTheme.shapes.r100)
            .background(
                containerColor,
                SafetyTheme.shapes.r100
            )
            .clickable(
                onClick = onClick
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                vertical = 4.dp, horizontal = 10.dp
            )
        ) {
            Text(
                text = text,
                style = style,
                color = contentColor,
            )
            icon?.let {
                Spacer(modifier = Modifier.width(2.dp))
                icon()
            }
        }
    }
}

@Composable
fun RegularButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = SafetyTheme.typography.paragraph1,
    isPositive: Boolean = true,
    enabled: Boolean = true,
) {
    Button(
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPositive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
        ),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        enabled = enabled,
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (isPositive) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun IconButtonPreview() {
    SafetyTheme {
        IconButton(
            text = "Button",
            icon = Icons.Default.Check,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun RegularButtonPreview() {
    SafetyTheme {
        RegularButton(
            text = "Button",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}