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
    }
}