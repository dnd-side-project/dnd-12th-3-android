package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.navigation.MainTab

@Composable
fun HomeBottomBar(
    bottomItems: List<MainTab>,
    onBottomItemClicked: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
    ) {
        HorizontalDivider()
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceDim, modifier = Modifier.height(42.dp)
        ) {
            bottomItems.forEach { item ->
                MainBottomBarItem(
                    tab = item,
                    selected = item == MainTab.Home,
                    onClick = {
                        onBottomItemClicked(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    modifier: Modifier = Modifier,
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.name,
            tint = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                Gray40
            },
            modifier = Modifier.size(22.dp),
        )
    }
}