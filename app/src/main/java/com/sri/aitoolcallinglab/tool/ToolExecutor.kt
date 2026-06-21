package com.sri.aitoolcallinglab.tool

class ToolExecutor(
    private val registry: ToolRegistry
) {
    suspend fun execute(toolName: String, input: String? = null): String {
        val tool = registry.findTool(toolName) ?: return "Tool Not Found"
        return tool.execute(input)
    }
}