package com.sri.geminiplayground

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.genai.Client
import com.google.genai.errors.ClientException
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel(
    val repository: MainRepository
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
                return MainViewModel(MainRepository(application)) as T
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
                repository.systemPrompt,
                repository.temperature,
                repository.topK,
                repository.topP
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
            repository.saveSystemPrompt(systemPrompt)
            repository.saveTemperature(temperature)
            repository.saveTopK(topK)
            repository.saveTopP(topP)
        }
    }

    fun onButtonClick(
        prompt: String
    ) {
        isPending.value = true
        viewModelScope.launch {
            response.value = geminiResponse(prompt)
            isPending.value = false
        }
    }

    suspend fun geminiResponse(
        prompt: String
    ): String = withContext(Dispatchers.IO) {
        var attempt = 0
        val maxAttempts = 3
        var currentDelay = 2000L

        while (attempt < maxAttempts) {
            try {
                val response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    config
                )
                return@withContext response.text() ?: "Empty response"
            } catch (e: ClientException) {
                val msg = e.message() ?: ""
                if (e.code() == 429) {
                    if (attempt < maxAttempts - 1) {
                        attempt++
                        kotlinx.coroutines.delay(currentDelay.milliseconds)
                        currentDelay *= 2
                        continue
                    }
                    return@withContext if (msg.contains("retry in")) {
                        "Quota exceeded. Retry in ${msg.substringAfter("retry in").trim()}."
                    } else {
                        "Quota exceeded. Please try again later."
                    }
                }
                return@withContext "Client error (${e.code()}): $msg"
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext "Error: ${e.message}"
            }
        }
        "Failed after $maxAttempts attempts"
    }
}