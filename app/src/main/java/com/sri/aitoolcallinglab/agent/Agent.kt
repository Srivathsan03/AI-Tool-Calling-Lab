package com.sri.aitoolcallinglab.agent

import android.util.Log
import com.sri.aitoolcallinglab.LlmClient

const val TAG = "Agent"

class Agent(
    private val llmClient: LlmClient,
    private val toolRegistry: ToolRegistry
) {

    suspend fun run(userMessage: String): AgentResult {
        val systemPrompt = ToolSelectionPromptBuilder.build(toolRegistry.getAllTools())
        val llmResponse = llmClient.generate(systemPrompt = systemPrompt, userPrompt = userMessage)
        Log.d(TAG, "run: llmResponse = $llmResponse")
        val toolCall = ToolCallParser.parse(llmResponse) ?: return AgentResult(
            answer = llmResponse,
            selectedTool = null,
            toolInput = null,
            toolResult = null
        )
        val tool = toolRegistry.findTool(toolCall.tool) ?: return AgentResult(
            answer = "Tool Not Found",
            selectedTool = null,
            toolInput = null,
            toolResult = null
        )
        val toolResult = tool.execute(input = toolCall.input)
        Log.d(TAG, "run: toolResult = $toolResult")
        val result = llmClient.generate(
            userPrompt =
                """
                User Question:
                $userMessage

                Tool Result:
                $toolResult

                Provide the final answer.
                """.trimIndent()
        )
        Log.d(TAG, "run: result = $result")
        val agentResult = AgentResult(
            answer = result,
            selectedTool = toolCall.tool,
            toolInput = toolCall.input,
            toolResult = toolResult
        )
        return agentResult
    }
}