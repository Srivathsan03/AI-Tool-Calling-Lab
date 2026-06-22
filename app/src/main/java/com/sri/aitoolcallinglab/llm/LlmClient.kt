package com.sri.aitoolcallinglab.llm

interface LlmClient {

    suspend fun generate(
        systemPrompt: String? = null,
        userPrompt: String
    ): String
}