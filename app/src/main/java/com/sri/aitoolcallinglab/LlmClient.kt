package com.sri.aitoolcallinglab

interface LlmClient {

    suspend fun generate(
        systemPrompt: String? = null,
        userPrompt: String
    ): String
}