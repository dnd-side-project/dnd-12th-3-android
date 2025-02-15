package com.dnd.safety.presentation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.IncidentTypeFilter
import com.dnd.safety.presentation.designsystem.theme.Gray20
import com.dnd.safety.presentation.designsystem.theme.Gray60
import com.dnd.safety.presentation.designsystem.theme.Gray70
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White

@Composable
fun IncidentsFilter(
    typeFilters: List<IncidentTypeFilter>,
    onFilterClick: (IncidentTypeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(40.dp)
    ) {
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
        items(typeFilters) { filter ->
            FilterButton(
                title = filter.incidentCategory.korTitle,
                isSelected = filter.isSelected,
                onClick = { onFilterClick(filter) }
            )
        }
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun FilterButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    SmallButton(
        text = title,
        onClick = onClick,
        style = if (isSelected) {
            SafetyTheme.typography.label5
        } else {
            SafetyTheme.typography.label4
        },
        borderColor = if (isSelected) {
            Gray70
        } else {
            Gray20
        },
        contentColor = if (isSelected) {
            White
        } else {
            Gray60
        },
        containerColor = if (isSelected) {
            Gray70
        } else {
            White
        }
    )
}

@Preview
@Composable
private fun IncidentsFilters() {
    SafetyTheme {
        IncidentsFilter(
            typeFilters = IncidentTypeFilter.entries,
            onFilterClick = {}
        )
    }
}