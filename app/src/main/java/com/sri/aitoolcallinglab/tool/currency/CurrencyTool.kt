package com.sri.aitoolcallinglab.tool.currency

import com.sri.aitoolcallinglab.tool.Tool

class CurrencyTool(
    private val currencyRepository: CurrencyRepository
) : Tool {

    override val name = "currency"
    override val description =
        "Converts currency from one to another. Expected format: amount FROM TO"

    override suspend fun execute(input: String?): String {
        if (input == null) return "Invalid input"
        val parts = input.split(" ")
        if (parts.size != 3) {
            return "Expected format: amount FROM TO"
        }
        val amount = parts[0].toDoubleOrNull() ?: return "Invalid amount"
        val fromCurrency = parts[1].uppercase()
        val toCurrency = parts[2].uppercase()
        return currencyRepository.convertCurrency(
            amount = amount,
            fromCurrency = fromCurrency,
            toCurrency = toCurrency
        )
    }
}