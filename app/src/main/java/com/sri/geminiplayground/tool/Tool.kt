package com.sri.geminiplayground.tool

interface Tool {
    val name: String
    fun execute(input: String? = null): String
}