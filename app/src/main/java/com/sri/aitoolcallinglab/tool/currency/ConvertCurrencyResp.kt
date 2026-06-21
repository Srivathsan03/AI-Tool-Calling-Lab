package com.sri.aitoolcallinglab.tool.currency

data class ConvertCurrencyResp(
    val amount: Int,
    val base: String,
    val rates: Map<String, Double>
)