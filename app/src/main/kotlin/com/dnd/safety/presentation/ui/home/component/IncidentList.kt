package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.presentation.theme.SafetyTheme
import java.time.LocalDateTime

@Composable
fun IncidentList(
    incidents: List<Incidents>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(incidents) { incident ->
            IncidentsItem(incidents = incident)
        }
    }
}

@Preview
@Composable
fun IncidentsItem(
    incidents: Incidents = Incidents(
        id = 1,
        title = "title",
        description = "description",
        pointX = 0.0,
        pointY = 0.0,
        imageUrl = "imageUrl",
        createdDate = LocalDateTime.now(),
        updatedDate = LocalDateTime.now()
    ),
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
private fun IncidentsSheetPreview() {
    SafetyTheme {
    }
}
