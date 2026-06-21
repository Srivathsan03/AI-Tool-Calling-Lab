package com.sri.geminiplayground.tool.currency

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("v1/latest")
    suspend fun convertCurrency(
        @Query("amount") amount: Double,
        @Query("from") fromCurrency: String,
        @Query("to") toCurrency: String
    ): ConvertCurrencyResp
}