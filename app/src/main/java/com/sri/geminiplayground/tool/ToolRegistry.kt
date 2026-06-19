package com.sri.geminiplayground.tool

class ToolRegistry(
    private val tools: Map<String, Tool>
) {
    fun findTool(toolName: String): Tool? {
        return tools[toolName]
    }
}