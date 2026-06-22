package com.sri.aitoolcallinglab.tool.time

import com.sri.aitoolcallinglab.tool.Tool
import com.sri.aitoolcallinglab.tool.ToolExample
import java.text.SimpleDateFormat
import java.util.Locale

class TimeTool : Tool {
    override val name = "time"
    override val description = "Returns the current time"
    override val guidance = "Use this tool to get the current time."
    override val examples: List<ToolExample> = listOf(
        ToolExample(
            userInput = "What is the current time?",
            toolName = "time",
            toolInput = "(not needed)"
        )
    )

    override suspend fun execute(input: String?): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(System.currentTimeMillis())
    }
}