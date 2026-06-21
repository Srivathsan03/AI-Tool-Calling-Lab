package com.sri.geminiplayground

import com.sri.geminiplayground.tool.Agent
import com.sri.geminiplayground.tool.ToolRegistry
import com.sri.geminiplayground.tool.calculator.CalculatorTool
import com.sri.geminiplayground.tool.currency.CurrencyRepository
import com.sri.geminiplayground.tool.currency.CurrencyTool
import com.sri.geminiplayground.tool.news.NewsRepository
import com.sri.geminiplayground.tool.news.NewsTool
import com.sri.geminiplayground.tool.weather.WeatherRepository
import com.sri.geminiplayground.tool.weather.WeatherTool

class ChatRunner {
    private val toolRegistry = ToolRegistry(
        mapOf(
            "calculator" to CalculatorTool(),
            "weather" to WeatherTool(WeatherRepository()),
            "currency" to CurrencyTool(CurrencyRepository()),
            "news" to NewsTool(NewsRepository())
        )
    )

    suspend fun getResponse(
        prompt: String,
    ): String {
        val agent = Agent(
            llmClient = GeminiLlmClient(repository = GeminiRepository()),
            toolRegistry = toolRegistry
        )
        return agent.run(prompt)
//        return when {
//            isMathExpression(prompt) -> {
//                toolExecutor.execute(toolName = "calculator", input = prompt)
//            }
//
//            isWeatherRequest(prompt) -> {
//                toolExecutor.execute(toolName = "weather", input = "Chennai")
//            }
//
//            isCurrencyRequest(prompt) -> {
//                toolExecutor.execute(toolName = "currency", input = prompt)
//            }
//
//            isNewsRequest(prompt) -> {
//                toolExecutor.execute(toolName = "news")
//            }
//
//            else -> {
//                repository.geminiResponse(
//                    prompt = prompt,
//                    config = config
//                )
//            }
//        }
    }

    private fun isMathExpression(prompt: String): Boolean {
        return Regex(
            """^\d+\s*[+\-*/]\s*\d+$"""
        ).matches(prompt)
    }

    private fun isWeatherRequest(prompt: String): Boolean {
        val lower = prompt.lowercase()
        return lower.contains("weather")
    }

    private fun isCurrencyRequest(prompt: String): Boolean {
        return Regex(
            """^\d+(\.\d+)?\s+[A-Za-z]{3}\s+[A-Za-z]{3}$"""
        ).matches(prompt)
    }

    private fun isNewsRequest(prompt: String): Boolean {
        val lower = prompt.lowercase()
        return lower.contains("news")
    }
}