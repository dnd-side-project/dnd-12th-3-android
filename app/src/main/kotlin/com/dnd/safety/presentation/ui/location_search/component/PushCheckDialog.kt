package com.dnd.safety.presentation.ui.location_search.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.component.DefaultDialog
import com.dnd.safety.presentation.designsystem.component.RegularButton
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun PushCheckDialog(
    onYesClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    DefaultDialog(
        content = {
            DialogContent(
                onYesClick = onYesClick,
                onDismissRequest = onDismissRequest
            )
        }
    )
}


@Composable
private fun DialogContent(
    onYesClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bell),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "알림 설정",
            style = SafetyTheme.typography.title1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "현재 위치와 주변의 위험 지역 정보를\n" +
                    "실시간으로 받아보세요!",
            style = SafetyTheme.typography.label2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = Gray80
        )
        Spacer(modifier = Modifier.height(8.dp))
        RegularButton(
            text = "알림을 받을게요",
            onClick = onYesClick,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "나중에 다시 알려주세요",
            style = SafetyTheme.typography.paragraph1,
            color = Gray50,
            modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = onDismissRequest)
        )
    }
}