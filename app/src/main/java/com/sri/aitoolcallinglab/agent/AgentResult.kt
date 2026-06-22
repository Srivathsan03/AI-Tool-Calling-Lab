package com.sri.aitoolcallinglab.agent

data class AgentResult(
    val answer: String,
    val selectedTool: String?,
    val toolInput: String?,
    val toolResult: String?
)
