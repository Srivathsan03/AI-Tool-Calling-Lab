package com.sri.geminiplayground

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.genai.Client
import com.google.genai.errors.ClientException
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel : ViewModel() {

    private val client by lazy {
        Client.builder()
            .apiKey(BuildConfig.GEMINI_API_KEY)
            .build()
    }
    val response: MutableState<String> = mutableStateOf("")
    val isPending: MutableState<Boolean> = mutableStateOf(false)
    val configPopupVisible: MutableState<Boolean> = mutableStateOf(false)
    lateinit var config: GenerateContentConfig

    fun onConfigClicked() {
        configPopupVisible.value = !configPopupVisible.value
    }

    fun createConfig(
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