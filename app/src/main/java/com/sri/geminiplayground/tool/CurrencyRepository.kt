package com.sri.geminiplayground.tool

class CurrencyRepository {

    suspend fun convertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String
    ): String {
        val response = CurrencyApiRetrofit.api.convertCurrency(
            amount = amount,
            fromCurrency = fromCurrency,
            toCurrency = toCurrency
        )
        return """
            $amount $fromCurrency = ${response.rates[toCurrency]} $toCurrency
        """.trimIndent()
    }
}