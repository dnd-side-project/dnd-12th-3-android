package com.dnd.safety.presentation.navigation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.navigation.MainBottomNavItem

@Composable
internal fun MainBottomBar(
    visible: Boolean,
    bottomItems: List<MainBottomNavItem>,
    currentItem: MainBottomNavItem?,
    onBottomItemClicked: (MainBottomNavItem) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) }
    ) {
        Column {
            HorizontalDivider()
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier.height(65.dp)
            ) {
                bottomItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = { onBottomItemClicked(item) },
                        selected = item == currentItem
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainBottomBarPreview() {
    SafetyTheme {
        MainBottomBar(
            visible = true,
            bottomItems = MainBottomNavItem.entries,
            currentItem = MainBottomNavItem.Home,
            onBottomItemClicked = { }
        )
    }
}