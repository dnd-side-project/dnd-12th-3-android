package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.component.circleBackground
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun HomeSearchBar(
    keyword: String,
    onShowSearchDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(60.dp),
        onClick = onShowSearchDialog,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
            )
            Text(
                text = keyword.ifBlank { "검색" },
                style = SafetyTheme.typography.paragraph1,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .size(26.dp)
                    .circleBackground()
                    .padding(2.dp)
            )
        }
    }
}

@Preview()
@Composable
private fun HomeSearchBarPreview() {
    SafetyTheme {
        HomeSearchBar(
            keyword = "Search",
            onShowSearchDialog = {}
        )
    }
}