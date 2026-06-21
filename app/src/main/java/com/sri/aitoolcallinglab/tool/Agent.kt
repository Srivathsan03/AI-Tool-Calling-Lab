package com.sri.aitoolcallinglab.tool

import com.sri.aitoolcallinglab.LlmClient

class Agent(
    private val llmClient: LlmClient,
    private val toolRegistry: ToolRegistry
) {

    suspend fun run(userMessage: String): String {
        val systemPrompt = ToolSelectionPromptBuilder.build(toolRegistry.getAllTools())
        val llmResponse = llmClient.generate(systemPrompt = systemPrompt, userPrompt = userMessage)
        val toolCall = ToolCallParser.parse(llmResponse) ?: return llmResponse
        val tool = toolRegistry.findTool(toolCall.tool) ?: return "Tool Not Found"
        val toolResult = tool.execute(input = toolCall.input)
        return llmClient.generate(
            userPrompt =
                """
                User Question:
                $userMessage

                Tool Result:
                $toolResult

                Provide the final answer.
                """.trimIndent()
        )
    }
}