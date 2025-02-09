package com.dnd.safety.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.presentation.designsystem.component.TextField
import com.dnd.safety.presentation.designsystem.theme.Gray50
import com.dnd.safety.presentation.designsystem.theme.Gray80
import com.dnd.safety.presentation.designsystem.theme.SafetyTheme
import com.dnd.safety.presentation.designsystem.theme.White
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction

@Composable
fun SearchDialog(
    onPlaceSelected: (LatLng, String) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: SearchDialogViewModel = hiltViewModel()
) {
    val predictions by viewModel.predictions.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray80)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = White,
                    modifier = Modifier
                        .clickable(onClick = onDismissRequest)
                        .padding(16.dp)
                        .size(25.dp)
                )
                TextField(
                    value = searchText,
                    onValueChange = viewModel::textChanged,
                    hint = "장소 검색",
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            LazyColumn {
                itemsIndexed(predictions) { index, prediction ->
                    PredictionItem(
                        prediction = prediction,
                        index = index,
                        lastIndex = predictions.lastIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.getPlaceLatLng(context, prediction.placeId) { latLng ->
                                    onPlaceSelected(latLng, prediction.getPrimaryText(null).toString())
                                }
                                onDismissRequest()
                            }
                            .padding(horizontal = 16.dp, vertical = 11.5.dp)
                            .animateItem()
                    )
                }
            }
        }
    }
}

@Composable
private fun PredictionItem(
    prediction: AutocompletePrediction,
    index: Int,
    lastIndex: Int,
    modifier: Modifier
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = prediction.getPrimaryText(null).toString(),
            style = SafetyTheme.typography.body1,
            modifier = modifier

        )
        if (index != lastIndex) {
            HorizontalDivider(
                color = Gray50,
            )
        }
    }
}