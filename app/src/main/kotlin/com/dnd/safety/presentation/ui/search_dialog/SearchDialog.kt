package com.dnd.safety.presentation.ui.search_dialog

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnd.safety.presentation.designsystem.component.TextField
import com.dnd.safety.presentation.designsystem.theme.Blue
import com.dnd.safety.presentation.designsystem.theme.Gray40
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
        SearchDialogContent(
            searchText = searchText,
            predictions = predictions,
            onTextChange = viewModel::textChanged,
            onPredictionClick = {
                viewModel.getPlaceLatLng(context, it.placeId) { latLng ->
                    onPlaceSelected(latLng, it.getPrimaryText(null).toString())
                }
                onDismissRequest()
                viewModel.textChanged("")
            },
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
fun SearchDialogContent(
    searchText: String,
    onTextChange: (String) -> Unit,
    predictions: List<AutocompletePrediction>,
    onPredictionClick: (AutocompletePrediction) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Gray40,
                modifier = Modifier
                    .clickable(onClick = onDismissRequest)
                    .padding(16.dp)
                    .size(25.dp)
            )
            TextField(
                value = searchText,
                onValueChange = onTextChange,
                hint = "주소 장소 검색",
                removeButton = true,
                focusRequester = focusRequester,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(24.dp))
        }
        LazyColumn {
            itemsIndexed(predictions) { index, prediction ->
                PredictionItem(
                    searchText = searchText,
                    prediction = prediction,
                    index = index,
                    lastIndex = predictions.lastIndex,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onPredictionClick(prediction)
                        }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .animateItem()
                )
            }
        }
    }

}

@Composable
private fun PredictionItem(
    searchText: String,
    prediction: AutocompletePrediction,
    index: Int,
    lastIndex: Int,
    modifier: Modifier
) {
    val primaryText = prediction.getPrimaryText(null).toString()
    val annotatedText = buildAnnotatedString {
        val startIndex = primaryText.indexOf(searchText, ignoreCase = true)
        if (startIndex != -1) {
            append(primaryText.substring(0, startIndex))
            withStyle(style = SpanStyle(color = Blue)) {
                append(primaryText.substring(startIndex, startIndex + searchText.length))
            }
            append(primaryText.substring(startIndex + searchText.length))
        } else {
            append(primaryText)
        }
    }

    Column {
        Text(
            text = annotatedText,
            style = SafetyTheme.typography.body1,
            modifier = modifier
        )
        if (index != lastIndex) {
            HorizontalDivider()
        }
    }
}


@Preview
@Composable
private fun SearchDialogPreview() {
    SafetyTheme {
        SearchDialogContent(
            searchText = "서울",
            onTextChange = {},
            predictions = emptyList(),
            onPredictionClick = {},
            onDismissRequest = {},
        )
    }
}