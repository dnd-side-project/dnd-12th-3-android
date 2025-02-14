package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.Incidents
import com.dnd.safety.domain.model.MediaFile
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray30
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.designsystem.theme.Gray60
import com.dnd.safety.presentation.designsystem.theme.Orange60
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun IncidentsItem(
    incidents: Incidents,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
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
                            Orange60,
                            SafetyTheme.shapes.r100
                        )
                ) {
                    Text(
                        text = incidents.incidentCategory.korTitle,
                        style = SafetyTheme.typography.label2,
                        color = White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = incidents.distance,
                    style = SafetyTheme.typography.label2,
                    color = Gray60
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = incidents.address,
                    style = SafetyTheme.typography.label2,
                    color = Gray40
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = incidents.userName,
                    style = SafetyTheme.typography.label2,
                    color = Gray30
                )
                Text(
                    text = "·",
                    style = SafetyTheme.typography.label2,
                    color = Gray30,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = incidents.daysAgo(),
                    style = SafetyTheme.typography.label2,
                    color = Gray30
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = incidents.description,
                style = SafetyTheme.typography.paragraph2
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IncidentsImages(
            imageUrls = incidents.mediaFiles
        )
    }
}

@Composable
private fun IncidentsImages(
    imageUrls: List<MediaFile>,
    modifier: Modifier = Modifier
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        item {
             Spacer(modifier = Modifier.width(8.dp))
        }
        if (imageUrls.size == 1) {
            item {
                IncidentsImage(
                    url = imageUrls.first().fileUrl,
                    modifier = Modifier
                        .width(screenWidthDp.dp - 32.dp)
                )
            }
        } else {
            items(imageUrls) { url ->
                IncidentsImage(
                    url = url.fileUrl,
                    modifier = Modifier
                        .width(screenWidthDp.dp / 2)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun IncidentsImage(
    url: String,
    modifier: Modifier = Modifier
) {
    CoilImage(
        imageModel = { url },
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(4.dp)),
        failure = {
            Box(
                modifier = modifier
                    .size(150.dp)
                    .background(Color.Red)
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun IncidentsSheetPreview() {
    SafetyTheme {
        IncidentsItem(
            incidents = Incidents.sampleIncidents.first()
        )
    }
}
