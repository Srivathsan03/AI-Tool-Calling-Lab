package com.sri.aitoolcallinglab.tool.date

import com.sri.aitoolcallinglab.tool.Tool
import com.sri.aitoolcallinglab.tool.ToolExample
import java.text.SimpleDateFormat
import java.util.Locale

class DateTool : Tool {
    override val name = "date"
    override val description = "Returns the current date"
    override val guidance = "Use this tool to get the current date."
    override val examples: List<ToolExample> = listOf(
        ToolExample(
            userInput = "What is the current date?",
            toolName = "date",
            toolInput = "(not needed)"
        )
    )

    override suspend fun execute(input: String?): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(System.currentTimeMillis())
    }
}