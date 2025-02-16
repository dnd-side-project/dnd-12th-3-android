package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
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
            containerColor = MaterialTheme.colorScheme.surfaceDim,
            modifier = Modifier.height(65.dp)
        ) {
            bottomItems.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "",
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Gray40,
                        unselectedTextColor = Gray,
                    ),
                    label = {
                    },
                    onClick = { onBottomItemClicked(item) },
                    selected = item == MainTab.Home
                )
            }
        }
    }
}