package com.sri.geminiplayground.tool

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CurrencyApiRetrofitProvider {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.frankfurter.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: CurrencyApi = retrofit.create(CurrencyApi::class.java)
}