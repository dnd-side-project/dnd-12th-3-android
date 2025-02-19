package com.dnd.safety.presentation.ui.myPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.presentation.designsystem.component.TopAppbar
import com.dnd.safety.presentation.designsystem.component.WatchOutButton
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.Gray70
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun SignOutRoute(
    onGoBack: () -> Unit,
    onShowSnackBar: (String) -> Unit
) {
    SignOutScreen(
        onGoBack = onGoBack,
        onSignOut = {
            onShowSnackBar("회원탈퇴 구현중")
        }
    )
}

@Composable
private fun SignOutScreen(
    onGoBack: () -> Unit,
    onSignOut: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = "",
                onBackEvent = onGoBack,
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                WatchOutButton(
                    text = "취소",
                    onClick = onGoBack,
                    enabled = false,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                WatchOutButton(
                    text = "확인",
                    onClick = onSignOut,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceDim,
        modifier = Modifier
            .navigationBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "정말 회원탈퇴를\n진행하시나요?",
                style = SafetyTheme.typography.title1,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "지금 탈퇴하시면 이런 기능들을 놓치게 돼요.",
                style = SafetyTheme.typography.label1,
                color = Gray50
            )
            Spacer(modifier = Modifier.height(36.dp))
            TextBox("실시간 재난/사고 알림")
            Spacer(modifier = Modifier.height(4.dp))
            TextBox("가족/지인에게 사고 알림 공유")
        }
    }
}

@Composable
private fun TextBox(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray10, shape = RoundedCornerShape(15.dp))
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = text,
            style = SafetyTheme.typography.paragraph2,
            color = Gray70
        )
    }
}

@Preview
@Composable
private fun SignOutScreenPreview() {
    SafetyTheme {
        SignOutScreen(
            onGoBack = { },
            onSignOut = { }
        )
    }
}