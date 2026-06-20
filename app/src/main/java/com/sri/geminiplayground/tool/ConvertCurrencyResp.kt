package com.sri.geminiplayground.tool

data class ConvertCurrencyResp(
    val amount: Int,
    val base: String,
    val rates: Map<String, Double>
)