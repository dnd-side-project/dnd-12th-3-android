package com.dnd.safety.presentation.ui.myPage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.MyTown
import com.dnd.safety.presentation.designsystem.theme.Black
import com.dnd.safety.presentation.designsystem.theme.Gray10
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White

@Composable
fun MyTownSheet(
    firstMyTown: MyTown?,
    secondMyTown: MyTown?,
    onAddClick: () -> Unit,
    onDeleteClick: (MyTown) -> Unit,
    onSelectClick: (MyTown) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "내 동네",
                style = SafetyTheme.typography.title2,
                color = Gray80
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "동네 저장은 최대 2개까지 가능해요!",
                style = SafetyTheme.typography.label1,
                color = Gray80
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                firstMyTown?.let {
                    MyTownItem(
                        myTown = it,
                        onDeleteClick = { onDeleteClick(it) },
                        onSelectClick = { onSelectClick(it) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (secondMyTown == null) {
                    MyTownAddButton(
                        onAddClick = onAddClick,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    MyTownItem(
                        myTown = secondMyTown,
                        onDeleteClick = { onDeleteClick(secondMyTown) },
                        onSelectClick = { onSelectClick(secondMyTown) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MyTownItem(
    myTown: MyTown,
    onDeleteClick: () -> Unit,
    onSelectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (myTown.selected) MaterialTheme.colorScheme.primary else Gray10,
        onClick = onSelectClick,
        modifier = modifier
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text =  myTown.title ?: myTown.address,
                color = if (myTown.selected) White else Black,
                style = SafetyTheme.typography.paragraph1,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "삭제하기",
                tint = if (myTown.selected) White else Gray40,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(onClick = onDeleteClick)
                    .padding(8.dp)
                    .size(16.dp)
            )
        }
    }
}

@Composable
private fun MyTownAddButton(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Gray10,
        onClick = onAddClick,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp),
        ) {
            Text(
                text = "",
                style = SafetyTheme.typography.paragraph1,
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "추가하기",
                tint = Gray40,
                modifier = Modifier
                    .padding(12.dp)
                    .size(16.dp)
                    .align(Alignment.Center)
            )
        }
    }
}