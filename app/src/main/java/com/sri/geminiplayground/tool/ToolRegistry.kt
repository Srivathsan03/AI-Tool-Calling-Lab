package com.sri.geminiplayground.tool

class ToolRegistry(
    private val tools: List<Tool>
) {
    fun findTool(toolName: String): Tool? {
        return tools.firstOrNull {
            it.name == toolName
        }
    }
}