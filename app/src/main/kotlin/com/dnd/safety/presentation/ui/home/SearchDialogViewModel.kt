package com.dnd.safety.presentation.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDialogViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    var searchText = MutableStateFlow("")
        private set


    private val _predictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions: StateFlow<List<AutocompletePrediction>> = _predictions

    private val placesClient = Places.createClient(context)

    init {
        viewModelScope.launch {
            searchText
                .debounce(300)
                .collectLatest {
                    if (it.isNotEmpty()) {
                        searchPlaces(it)
                    }
                }
        }
    }

    fun textChanged(text: String) {
        searchText.update { text }
    }

    private fun searchPlaces(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setCountries("KR")
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                _predictions.value = response.autocompletePredictions
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesAPI", "Error: ${exception.message}")
            }
    }

    fun getPlaceLatLng(context: Context, placeId: String, onLatLngReceived: (LatLng) -> Unit) {
        val placesClient = Places.createClient(context)
        val request = FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG)).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                response.place.latLng?.let { onLatLngReceived(it) }
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesAPI", "Place fetch failed: ${exception.message}")
            }
    }
}