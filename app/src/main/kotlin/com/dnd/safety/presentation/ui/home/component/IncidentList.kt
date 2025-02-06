package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.presentation.designsystem.component.IconButton
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun IncidentList(
    incidents: List<Incidents>,
    filter: String,
    onFilterClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        ListButtons(
            filter = filter,
            onFilterClick = onFilterClick,
            onSearchClick = onSearchClick,
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(incidents) { incident ->
                IncidentsItem(incidents = incident)
            }
        }
    }
}

@Composable
private fun ListButtons(
    filter: String,
    onFilterClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        IconButton(
            text = filter,
            icon = Icons.Default.KeyboardArrowDown,
            onClick = onFilterClick,
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            text = "내용 검색",
            icon = Icons.Default.Search,
            onClick = onSearchClick,
        )
    }
}

@Preview
@Composable
private fun ListButtonsPreview() {
    SafetyTheme {
        ListButtons(
            filter = "최신순",
            onFilterClick = {},
            onSearchClick = {}
        )
    }
}