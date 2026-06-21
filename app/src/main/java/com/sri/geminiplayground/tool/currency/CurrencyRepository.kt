package com.sri.geminiplayground.tool.currency

class CurrencyRepository {

    suspend fun convertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String
    ): String {
        val response = CurrencyApiRetrofitProvider.api.convertCurrency(
            amount = amount,
            fromCurrency = fromCurrency,
            toCurrency = toCurrency
        )
        return """
            $amount $fromCurrency = ${response.rates[toCurrency]} $toCurrency
        """.trimIndent()
    }
}