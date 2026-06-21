package com.sri.aitoolcallinglab.tool

object ToolCallParser {

    fun parse(response: String): ToolCall? {
        val lines = response.lines()

        val tool = lines
            .firstOrNull { it.startsWith("TOOL:") }
            ?.substringAfter("TOOL:")
            ?.trim()

        val input = lines
            .firstOrNull { it.startsWith("INPUT:") }
            ?.substringAfter("INPUT:")
            ?.trim()

        return if (
            tool != null &&
            input != null
        ) {
            ToolCall(tool = tool, input = input)
        } else {
            null
        }
    }
}