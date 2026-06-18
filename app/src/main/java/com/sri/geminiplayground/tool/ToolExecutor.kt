package com.sri.geminiplayground.tool

class ToolExecutor(
    private val registry: ToolRegistry
) {
    fun execute(toolName: String, input: String? = null): String {
        val tool = registry.findTool(toolName)
        return tool?.execute(input) ?: "Tool Not Found"
    }
}