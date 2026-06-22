package com.sri.aitoolcallinglab.agent

data class ToolCall(
    val tool: String,
    val input: String
)