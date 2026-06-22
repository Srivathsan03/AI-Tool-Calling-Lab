package com.sri.aitoolcallinglab.chat

import com.sri.aitoolcallinglab.agent.Agent
import com.sri.aitoolcallinglab.agent.AgentResult
import com.sri.aitoolcallinglab.llm.GeminiLlmClient
import com.sri.aitoolcallinglab.llm.GeminiRepository
import com.sri.aitoolcallinglab.tool.ToolRegistry
import com.sri.aitoolcallinglab.tool.calculator.CalculatorTool
import com.sri.aitoolcallinglab.tool.currency.CurrencyRepository
import com.sri.aitoolcallinglab.tool.currency.CurrencyTool
import com.sri.aitoolcallinglab.tool.date.DateTool
import com.sri.aitoolcallinglab.tool.news.NewsRepository
import com.sri.aitoolcallinglab.tool.news.NewsTool
import com.sri.aitoolcallinglab.tool.time.TimeTool
import com.sri.aitoolcallinglab.tool.weather.WeatherRepository
import com.sri.aitoolcallinglab.tool.weather.WeatherTool

class ChatRunner {

    private val repository = GeminiRepository()
    private val toolRegistry = ToolRegistry(
        mapOf(
            "calculator" to CalculatorTool(),
            "weather" to WeatherTool(WeatherRepository()),
            "currency" to CurrencyTool(CurrencyRepository()),
            "news" to NewsTool(NewsRepository()),
            "date" to DateTool(),
            "time" to TimeTool()
        )
    )

    suspend fun getResponse(
        prompt: String,
    ): AgentResult {
        val agent = Agent(
            llmClient = GeminiLlmClient(repository = repository),
            toolRegistry = toolRegistry
        )
        return agent.run(prompt)
    }
}