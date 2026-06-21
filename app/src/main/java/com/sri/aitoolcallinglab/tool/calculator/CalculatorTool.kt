package com.sri.aitoolcallinglab.tool.calculator

import com.sri.aitoolcallinglab.tool.Tool

class CalculatorTool : Tool {

    override val name = "calculator"
    override val description =
        "Performs basic math calculations. Expected format: number symbol number"

    override suspend fun execute(input: String?): String {
        if (input.isNullOrBlank()) {
            return "Input cannot be empty"
        }
        val parts = input.trim().split(" ")
        if (parts.size != 3) {
            return "Invalid expression"
        }
        val left = parts[0].toDoubleOrNull() ?: return "Invalid number"
        val operator = parts[1]
        val right = parts[2].toDoubleOrNull() ?: return "Invalid number"

        val result = when (operator) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> {
                if (right == 0.0) {
                    return "Cannot divide by zero"
                }
                left / right
            }

            else -> return "Unsupported operator"
        }
        return result.toString()
    }
}