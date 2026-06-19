package com.sri.geminiplayground

import com.google.genai.Client
import com.google.genai.types.GenerateContentConfig
import com.sri.geminiplayground.tool.CalculatorTool
import com.sri.geminiplayground.tool.ToolExecutor
import com.sri.geminiplayground.tool.ToolRegistry
import com.sri.geminiplayground.tool.WeatherRepository
import com.sri.geminiplayground.tool.WeatherTool

class ChatRunner(
    val repository: MainRepository
) {
    private val toolExecutor = ToolExecutor(
        ToolRegistry(
            mapOf(
                "calculator" to CalculatorTool(),
                "weather" to WeatherTool(WeatherRepository())
            )
        )
    )

    suspend fun getResponse(
        model: AIModel,
        prompt: String,
        client: Client,
        config: GenerateContentConfig
    ): String {
        return when {
            isMathExpression(prompt) -> {
                toolExecutor.execute(toolName = "calculator", input = prompt)
            }

            isWeatherRequest(prompt) -> {
                toolExecutor.execute(toolName = "weather", input = "Chennai")
            }

            else -> {
                repository.geminiResponse(
                    model = model,
                    prompt = prompt,
                    client = client,
                    config = config
                )
            }
        }
    }

    private fun isMathExpression(
        prompt: String
    ): Boolean {
        return Regex("""^\d+\s*[+\-*/]\s*\d+$""").matches(prompt)
    }

    private fun isWeatherRequest(
        prompt: String
    ): Boolean {
        val lower = prompt.lowercase()
        return lower.contains("weather")
    }
}