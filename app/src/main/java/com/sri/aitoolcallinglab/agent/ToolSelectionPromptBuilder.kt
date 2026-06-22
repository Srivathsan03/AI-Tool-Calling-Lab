package com.sri.aitoolcallinglab.agent

import com.sri.aitoolcallinglab.tool.Tool

object ToolSelectionPromptBuilder {

    fun build(tools: List<Tool>): String {
        return buildString {
            appendLine("You are an AI assistant with access to tools.")
            appendLine()

            appendLine("Available tools:")
            tools.forEach { tool ->
                appendLine("- ${tool.name}: ${tool.description}")
                appendLine()
                appendLine(tool.guidance)
                tool.examples.forEach { toolExample ->
                    appendLine("Example:")
                    appendLine("User: ${toolExample.userInput}")
                    appendLine("TOOL: ${toolExample.toolName}")
                    appendLine("INPUT: ${toolExample.toolInput}")
                    appendLine()
                }
                appendLine()
            }

            appendLine("Rules:")
            appendLine("If a tool is required respond with:")

            appendLine("TOOL: <tool_name>")
            appendLine("INPUT: <tool_input>")
            appendLine()

            appendLine("Do not explain your decision.")
            appendLine("Do not include markdown.")
            appendLine("Do not include additional text.")

            appendLine("If no tool is needed, answer normally.")
        }
    }
}