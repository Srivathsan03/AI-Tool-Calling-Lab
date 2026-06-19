package com.sri.geminiplayground

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.genai.Client
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(
    val chatRunner: ChatRunner
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return MainViewModel(ChatRunner(MainRepository(application))) as T
            }
        }
    }

    private val client by lazy {
        Client.builder()
            .apiKey(BuildConfig.GEMINI_API_KEY)
            .build()
    }
    val response: MutableState<String> = mutableStateOf("")
    val isPending: MutableState<Boolean> = mutableStateOf(false)
    val configPopupVisible: MutableState<Boolean> = mutableStateOf(false)
    lateinit var config: GenerateContentConfig

    init {
        viewModelScope.launch {
            combine(
                chatRunner.repository.systemPrompt,
                chatRunner.repository.temperature,
                chatRunner.repository.topK,
                chatRunner.repository.topP
            ) { systemPrompt, temperature, topK, topP ->
                updateConfig(systemPrompt, temperature, topK, topP)
            }.collect { }
        }
    }

    fun onConfigClicked() {
        configPopupVisible.value = !configPopupVisible.value
    }

    private fun updateConfig(
        systemPrompt: String,
        temperature: Float,
        topK: Float,
        topP: Float
    ) {
        config = GenerateContentConfig.builder()
            .temperature(temperature)
            .topK(topK)
            .topP(topP)
            .systemInstruction(
                Content.fromParts(
                    Part.fromText(
                        systemPrompt
                    )
                )
            )
            .build()
    }

    fun saveConfig(
        systemPrompt: String = "",
        temperature: Float = 0.1f,
        topK: Float = 5f,
        topP: Float = .9f
    ) {
        viewModelScope.launch {
            chatRunner.repository.saveSystemPrompt(systemPrompt)
            chatRunner.repository.saveTemperature(temperature)
            chatRunner.repository.saveTopK(topK)
            chatRunner.repository.saveTopP(topP)
        }
    }

    fun onButtonClick(
        prompt: String
    ) {
        isPending.value = true
        viewModelScope.launch {
            response.value = chatRunner.getResponse(
                model = AIModel.GEMINI_3_1_FLASH_LITE,
                prompt = prompt,
                client = client,
                config = config
            )
            isPending.value = false
        }
    }
}