package com.sri.aitoolcallinglab.agent

class ToolRegistry(
    private val tools: Map<String, Tool>
) {
    fun findTool(toolName: String): Tool? {
        return tools[toolName]
    }

    fun getAllTools(): List<Tool> {
        return tools.values.toList()
    }
}