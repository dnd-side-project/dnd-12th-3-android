package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.presentation.designsystem.component.IconButton
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun IncidentList(
    incidents: List<Incidents>,
    sort: String,
    typeFilters: List<IncidentTypeFilter>,
    onSortClick: () -> Unit,
    onFilterClick: (IncidentTypeFilter) -> Unit,
    onSearchClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        IncidentsFilter(
            typeFilters = typeFilters,
            onFilterClick = onFilterClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ListButtons(
            sort = sort,
            onSortClick = onSortClick,
            onSearchClick = onSearchClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(incidents) { incident ->
                IncidentsItem(incidents = incident)
            }
        }
    }
}

@Composable
private fun ListButtons(
    sort: String,
    onSortClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(
            text = sort,
            icon = Icons.Default.KeyboardArrowDown,
            onClick = onSortClick,
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            text = "내용 검색",
            icon = Icons.Default.Search,
            onClick = onSearchClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IncidentListPreview() {
    SafetyTheme {
        IncidentList(
            incidents = Incidents.sampleIncidents,
            sort = "최신순",
            typeFilters = IncidentTypeFilter.entries,
            onFilterClick = {},
            onSortClick = {},
            onSearchClick = {},
        )
    }
}