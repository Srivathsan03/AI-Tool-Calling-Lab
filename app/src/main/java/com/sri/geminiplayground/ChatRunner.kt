package com.sri.geminiplayground

import com.google.genai.Client
import com.google.genai.types.GenerateContentConfig
import com.sri.geminiplayground.tool.CalculatorTool
import com.sri.geminiplayground.tool.ToolExecutor
import com.sri.geminiplayground.tool.ToolRegistry

class ChatRunner(
    val repository: MainRepository
) {
    private val toolExecutor = ToolExecutor(
        ToolRegistry(
            mapOf("calculator" to CalculatorTool())
        )
    )
    suspend fun getResponse(
        model: AIModel,
        prompt: String,
        client: Client,
        config: GenerateContentConfig
    ): String {
        return if (isMathExpression(prompt)) {
            toolExecutor.execute(toolName = "calculator", input = prompt)
        } else {
            repository.geminiResponse(
                model = model,
                prompt = prompt,
                client = client,
                config = config
            )
        }
    }

    private fun isMathExpression(
        prompt: String
    ): Boolean {
        return Regex(
            """^\d+\s*[+\-*/]\s*\d+$"""
        ).matches(prompt)
    }
}