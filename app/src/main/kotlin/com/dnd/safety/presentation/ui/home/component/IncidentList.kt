package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.presentation.designsystem.component.IncidentsFilter
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun IncidentList(
    incidents: List<Incidents>,
    typeFilters: List<IncidentTypeFilter>,
    onFilterClick: (IncidentTypeFilter) -> Unit,
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
        HorizontalDivider(color = Gray10)
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

@Preview(showBackground = true)
@Composable
private fun IncidentListPreview() {
    SafetyTheme {
        IncidentList(
            incidents = Incidents.sampleIncidents,
            typeFilters = IncidentTypeFilter.entries,
            onFilterClick = {},
        )
    }
}