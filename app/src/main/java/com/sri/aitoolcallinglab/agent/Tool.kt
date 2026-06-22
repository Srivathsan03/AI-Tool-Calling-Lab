package com.sri.aitoolcallinglab.agent

interface Tool {
    val name: String
    val description: String
    val guidance: String
    val examples: List<ToolExample>
    suspend fun execute(input: String? = null): String
}