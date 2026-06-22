package com.sri.aitoolcallinglab.tool

interface Tool {
    val name: String
    val description: String
    val guidance: String
    val examples: String
    suspend fun execute(input: String? = null): String
}