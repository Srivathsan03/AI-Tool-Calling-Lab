package com.sri.geminiplayground

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.genai.Client
import com.google.genai.errors.ClientException
import com.google.genai.types.GenerateContentConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel : ViewModel() {

    val response: MutableState<String> = mutableStateOf("")
    var isPending: MutableState<Boolean> = mutableStateOf(false)

    fun onButtonClick(
        prompt:String,
        temperature: Float,
        topK: Float,
        topP: Float
    ) {
        isPending.value = true
        viewModelScope.launch {
            response.value = geminiResponse(prompt, temperature, topK, topP)
            isPending.value = false
        }
    }

    suspend fun geminiResponse(
        prompt: String,
        temperature: Float,
        topK: Float,
        topP: Float
    ): String = withContext(Dispatchers.IO) {
        val client = Client.builder()
            .apiKey(BuildConfig.GEMINI_API_KEY)
            .build()

        var attempt = 0
        val maxAttempts = 3
        var currentDelay = 2000L

        while (attempt < maxAttempts) {
            try {
                val response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    GenerateContentConfig.builder()
                        .temperature(temperature)
                        .topK(topK)
                        .topP(topP)
                        .build()
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