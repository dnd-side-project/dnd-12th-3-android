package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun TopAppbar(
    modifier: Modifier = Modifier,
    title: String = "",
    titleStyle: TextStyle = SafetyTheme.typography.paragraph1,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceDim,
    iconId: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    navigationType: TopAppbarType = TopAppbarType.Default,
    onBackEvent: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(horizontal = 4.dp)
            .padding(top = 4.dp)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onBackEvent)
                .align(Alignment.CenterStart)
        ) {
            TopAppbarIcon(
                tint = contentColor,
                icon = iconId,
                onClick = onBackEvent
            )
        }

        Text(
            text = title,
            color = contentColor,
            style = titleStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Center)
        )

        Box(
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            when (navigationType) {
                is TopAppbarType.Custom -> {
                    navigationType.content()
                }
                is TopAppbarType.IconButton -> {
                    TopAppbarIcon(
                        tint = contentColor,
                        icon = navigationType.icon,
                        onClick = navigationType.onClick
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun TopAppbarIcon(
    tint: Color,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "menu Icon",
            tint = tint,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

sealed interface TopAppbarType {
    data object Default : TopAppbarType
    data class Custom(val content: @Composable () -> Unit) : TopAppbarType
    data class IconButton(val icon: ImageVector, val onClick: () -> Unit) : TopAppbarType
}
