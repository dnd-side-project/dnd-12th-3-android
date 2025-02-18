package com.dnd.safety.presentation.ui.myPage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SIgnOutViewModel @Inject constructor(

) : ViewModel() {

    var name: String = ""
        private set
}