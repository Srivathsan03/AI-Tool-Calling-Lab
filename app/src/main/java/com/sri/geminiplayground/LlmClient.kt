package com.sri.geminiplayground

interface LlmClient {

    suspend fun generate(
        systemPrompt: String? = null,
        userPrompt: String
    ): String
}