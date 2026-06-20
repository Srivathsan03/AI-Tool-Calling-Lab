package com.sri.geminiplayground.tool

class CurrencyTool(
    private val currencyRepository: CurrencyRepository
) : Tool {

    override val name = "currency"

    override suspend fun execute(input: String?): String {
        if(input == null) return "Invalid input"
        val parts = input.split(" ")
        if(parts.size != 3) {
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