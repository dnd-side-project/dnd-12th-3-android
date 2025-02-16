package com.dnd.safety.presentation.ui.my_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dnd.safety.R
import com.dnd.safety.presentation.designsystem.component.Switch
import com.dnd.safety.presentation.designsystem.component.TextFieldBox
import com.dnd.safety.presentation.designsystem.component.TopAppbar
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.Main
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@Composable
fun MyPageRoute(
    onGoBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppbar(
                title = "마이페이지",
                onBackEvent = onGoBack
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            MyPageContent()
        }
    }
}

@Composable
fun MyPageContent(

) {
    MyPageScreen(
        name = "홍길동"
    )
}

@Composable
fun MyPageScreen(
    name: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_my_page),
                    contentDescription = "My Page Image",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(12.5.dp))
                Text(
                    text = "$name 님",
                    style = SafetyTheme.typography.title2
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            MyPageButton(
                text = "작성 글 보기",
                onClick = {}
            )
            Spacer(modifier = Modifier.size(4.dp))
            MyPageButton(
                text = "내 동네 설정",
                onClick = {}
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextFieldBox(
                text = "알림 설정",
                textStyle = SafetyTheme.typography.label1,
                shape = RoundedCornerShape(15.dp),
                paddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            ) {
                Switch(
                    checked = true,
                    onCheckedChange = {},
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 16.dp)
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(color = Gray10, shape = RoundedCornerShape(15.dp))
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("주변의 사고/재난 정보를 실시간으로\n받기 위해 반드시 ")
                        withStyle(style = SpanStyle(color = Main)) {
                            append("알림을 켜주세요!")
                        }
                    },
                    color = Gray50,
                    style = SafetyTheme.typography.label1,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "로그아웃",
                    style = SafetyTheme.typography.label3,
                    color = Gray50,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "회원탈퇴",
                    style = SafetyTheme.typography.label3,
                    color = Gray50,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun MyPageButton(
    text: String,
    onClick: () -> Unit
) {
    TextFieldBox(
        text = text,
        shape = RoundedCornerShape(15.dp),
        textStyle = SafetyTheme.typography.label1,
        paddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    SafetyTheme {
        MyPageScreen(
            name = "홍길동"
        )
    }
}