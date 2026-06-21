package com.sri.geminiplayground.tool

object ToolSelectionPromptBuilder {

    fun build(tools: List<Tool>): String {
        return buildString {
            appendLine("You are an AI assistant with access to tools.")
            appendLine()

            appendLine("Available tools:")
            tools.forEach { tool ->
                appendLine("- ${tool.name}: ${tool.description}")
                appendLine()
            }

            appendLine("Rules:")
            appendLine("If a tool is required respond with:")

            appendLine("TOOL: <tool_name>")
            appendLine("INPUT: <tool_input>")
            appendLine()

            appendLine("Do not explain your decision.")

            appendLine("For calculator tool:")
            appendLine("Always format expressions with spaces between numbers and operators.")
            appendLine("Example: \"2 + 2\", not \"2+2\"")

            appendLine("For weather tool:")
            appendLine("Always format expressions with spaces between city, latitude and longitude.")
            appendLine("Example: chennai x.00 y.00")

            appendLine("If no tool is needed, answer normally.")
        }
    }
}