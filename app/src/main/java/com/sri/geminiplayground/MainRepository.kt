package com.sri.geminiplayground

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
}