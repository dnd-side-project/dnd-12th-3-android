package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.presentation.designsystem.component.IncidentsFilter
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun IncidentList(
    incidents: List<Incident>,
    typeFilters: List<IncidentTypeFilter>,
    onFilterClick: (IncidentTypeFilter) -> Unit,
    onShowDetail: (Incident) -> Unit,
    onLike: (Incident) -> Unit,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(incidents) { incident ->
                    IncidentsItem(
                        incident = incident,
                        onShowComment = {
                            onShowDetail(incident)
                        },
                        onLike = {
                            onLike(incident)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IncidentListPreview() {
    SafetyTheme {
        IncidentList(
            incidents = Incident.sampleIncidents,
            typeFilters = IncidentTypeFilter.entries,
            onFilterClick = {},
            onShowDetail = {},
            onLike = {}
        )
    }
}