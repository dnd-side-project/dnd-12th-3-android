package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray30
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun IncidentsItem(
    incidents: Incidents = Incidents.sampleIncidents.first(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = incidents.title,
                style = SafetyTheme.typography.title3
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(SafetyTheme.shapes.r100)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        SafetyTheme.shapes.r100
                    )
            ) {
                Text(
                    text = incidents.incidentType.name,
                    style = SafetyTheme.typography.smallText,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = incidents.distance,
                style = SafetyTheme.typography.label1,
                color = Gray10
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = incidents.address,
                style = SafetyTheme.typography.label1,
                color = Gray30
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = incidents.userName,
                style = SafetyTheme.typography.label1,
                color = Gray30
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = incidents.daysAgo,
                style = SafetyTheme.typography.label1,
                color = Gray30
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = incidents.description,
            style = SafetyTheme.typography.paragraph2
        )
        Spacer(modifier = Modifier.height(16.dp))
        IncidentsImages(
            imageUrls = incidents.imageUrls
        )
    }
}

@Composable
private fun IncidentsImages(
    imageUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        if (imageUrls.size == 1) {
            item {
                IncidentsImage(
                    url = imageUrls.first(),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        items(imageUrls) { url ->
            IncidentsImage(
                url = url,
                modifier = Modifier
                    .width(screenWidthDp.dp / 2)
            )
        }
    }
}

@Composable
fun IncidentsImage(
    url: String,
    modifier: Modifier = Modifier
) {
    CoilImage(
        imageModel = { url },
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(4.dp)),
        failure = {
            Text(text = "image request failed.")
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun IncidentsSheetPreview() {
    SafetyTheme {
        IncidentsItem()
    }
}
