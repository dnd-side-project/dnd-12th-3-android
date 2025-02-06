package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
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
    containerColor: Color = Gray70,
    contentColor: Color = Gray20
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
                style = SafetyTheme.typography.smallText,
                color = contentColor,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .size(15.dp)

            )
        }
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