package com.sri.geminiplayground

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.genai.Client
import com.google.genai.errors.ClientException
import com.google.genai.types.GenerateContentConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

val Context.dataStore by preferencesDataStore(
    name = "Settings"
)

object PreferenceKeys {
    val SYSTEM_PROMPT =
        stringPreferencesKey("system_prompt")

    val TEMPERATURE =
        floatPreferencesKey("temperature")

    val TOP_P =
        floatPreferencesKey("top_p")

    val TOP_K =
        floatPreferencesKey("top_k")
}

class MainRepository(
    private val context: Context
) {
    suspend fun saveSystemPrompt(
        systemPrompt: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SYSTEM_PROMPT] = systemPrompt
        }
    }

    suspend fun saveTemperature(
        temperature: Float
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.TEMPERATURE] = temperature
        }
    }

    suspend fun saveTopK(
        topK: Float
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.TOP_K] = topK
        }
    }

    suspend fun saveTopP(
        topP: Float
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.TOP_P] = topP
        }
    }

    val systemPrompt: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.SYSTEM_PROMPT] ?: ""
    }
    val temperature: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.TEMPERATURE] ?: 0.1f
    }
    val topK: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.TOP_K] ?: 50f
    }
    val topP: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.TOP_P] ?: 0.9f
    }

    suspend fun geminiResponse(
        model: AIModel,
        prompt: String,
        client: Client,
        config: GenerateContentConfig
    ): String = withContext(Dispatchers.IO) {
        var attempt = 0
        val maxAttempts = 3
        var currentDelay = 2000L

        while (attempt < maxAttempts) {
            try {
                val response = client.models.generateContent(
                    model.modelId,
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