package com.sri.geminiplayground

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val chatRunner = ChatRunner()

    val response: MutableState<String> = mutableStateOf("")
    val isPending: MutableState<Boolean> = mutableStateOf(false)

    fun onButtonClick(
        prompt: String
    ) {
        isPending.value = true
        viewModelScope.launch {
            response.value = chatRunner.getResponse(
                prompt = prompt
            )
            isPending.value = false
        }
    }
}