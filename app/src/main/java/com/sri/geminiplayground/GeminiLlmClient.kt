package com.sri.geminiplayground

import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part

class GeminiLlmClient(
    private val repository: GeminiRepository
) : LlmClient {
    override suspend fun generate(
        systemPrompt: String?,
        userPrompt: String
    ): String {
        val config = GenerateContentConfig.builder()
            .systemInstruction(
                Content.fromParts(
                    Part.fromText(
                        systemPrompt ?: ""
                    )
                )
            )
            .build()
        return repository.geminiResponse(
            prompt = userPrompt,
            config = config
        )
    }
}