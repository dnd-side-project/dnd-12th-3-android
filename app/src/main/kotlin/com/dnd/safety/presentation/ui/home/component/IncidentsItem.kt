package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dnd.safety.R
import com.dnd.safety.domain.model.Incident
import com.dnd.safety.domain.model.MediaFile
import com.dnd.safety.presentation.designsystem.theme.Gray30
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.designsystem.theme.Gray60
import com.dnd.safety.presentation.designsystem.theme.Orange60
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.dnd.safety.utils.daysAgo
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun IncidentsItem(
    incident: Incident,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
    imageHeight: Dp = 200.dp,
    onShowComment: (() -> Unit)? = null,
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
                    text = incident.title,
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
                        text = incident.incidentCategory.korTitle,
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
                    text = incident.distance,
                    style = SafetyTheme.typography.label2,
                    color = Gray60
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = incident.roadNameAddress,
                    style = SafetyTheme.typography.label2,
                    color = Gray40
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = incident.userName,
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
                    text = incident.createdDate.daysAgo(),
                    style = SafetyTheme.typography.label2,
                    color = Gray30
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = incident.description,
                style = SafetyTheme.typography.paragraph2
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IncidentsImages(
            imageUrls = incident.mediaFiles,
            imageHeight = imageHeight
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row {
                onShowComment?.let {
                    IconWithCount(
                        icon = R.drawable.ic_message,
                        count = incident.commentCount,
                        modifier = Modifier
                            .clickable(
                                onClick = onShowComment
                            )
                            .padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                IconWithCount(
                    icon = R.drawable.ic_like,
                    count = incident.likeCount,
                    modifier = Modifier
                        .clickable(
                            onClick = onLike
                        )
                        .padding(vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            onShowComment?.let {
                Text(
                    text = "댓글 쓰기",
                    style = SafetyTheme.typography.paragraph2,
                    color = Gray30,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onShowComment)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun IncidentsImages(
    imageUrls: List<MediaFile>,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        item {
            Spacer(modifier = Modifier.width(8.dp))
        }
        if (imageUrls.size == 1) {
            item {
                IncidentsImage(
                    url = imageUrls.first().fileUrl,
                    imageHeight = imageHeight,
                    modifier = Modifier
                        .width(screenWidthDp.dp - 32.dp)
                )
            }
        } else {
            items(imageUrls) { url ->
                IncidentsImage(
                    url = url.fileUrl,
                    imageHeight = imageHeight,
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
    imageHeight: Dp,
    modifier: Modifier = Modifier
) {
    CoilImage(
        imageModel = { url },
        modifier = modifier
            .height(imageHeight)
            .clip(RoundedCornerShape(4.dp)),
        failure = {
            Box(
                modifier = modifier
                    .size(imageHeight)
                    .background(Color.Red)
            )
        }
    )
}

@Composable
private fun IconWithCount(
    icon: Int,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Gray40,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = count.toString(),
            style = SafetyTheme.typography.label5,
            color = Gray40
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun IncidentsSheetPreview() {
    SafetyTheme {
        IncidentsItem(
            incident = Incident.sampleIncidents.first(),
            onShowComment = { },
            onLike = { }
        )
    }
}
