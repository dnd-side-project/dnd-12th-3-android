package com.dnd.safety.presentation.ui.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.dnd.safety.domain.model.SortFilter
import com.dnd.safety.presentation.designsystem.component.BottomSheet
import com.dnd.safety.presentation.designsystem.theme.Gray40
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortSheet(
    sortFilters: List<SortFilter>,
    onSelectSort: (SortFilter) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
) {
    BottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Text(
            text = "정렬",
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            style = SafetyTheme.typography.title1
        )
        sortFilters.forEach {
            SortItem(
                sortFilter = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelectSort(it)
                        onDismissRequest()
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SortItem(
    sortFilter: SortFilter,
    modifier: Modifier = Modifier
) {
    val color = if (sortFilter.isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Gray40
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = sortFilter.sort.text,
            color = color
        )
        Spacer(modifier = Modifier.weight(1f))
        if (sortFilter.isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SortSheetPreview() {
    SafetyTheme {
        SortSheet(
            sheetState = SheetState(true, Density(1f), SheetValue.Expanded, { true }, false),
            sortFilters = SortFilter.entries,
            onSelectSort = {},
            onDismissRequest = {}
        )
    }
}