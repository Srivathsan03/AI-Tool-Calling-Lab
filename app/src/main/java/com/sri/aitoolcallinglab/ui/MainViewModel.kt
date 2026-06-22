package com.sri.aitoolcallinglab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sri.aitoolcallinglab.chat.ChatRunner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val chatRunner: ChatRunner
) : ViewModel() {

    private val _uiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onButtonClick(
        prompt: String
    ) {
        _uiState.value = _uiState.value.copy(isPending = true)
        viewModelScope.launch {
            try {
                val result = chatRunner.getResponse(prompt = prompt)
                _uiState.value = _uiState.value.copy(
                    response = result.answer,
                    isPending = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    response = "Error: ${e.message}",
                    isPending = false
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(ChatRunner()) as T
            }
        }
    }
}