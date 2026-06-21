package com.sri.geminiplayground.tool

interface Tool {
    val name: String
    val description: String
    suspend fun execute(input: String? = null): String
}