package com.sri.geminiplayground.tool

class HelloTool : Tool {

    override val name = "hello"

    override fun execute(input: String?): String {
        return "Hello from $name"
    }
}