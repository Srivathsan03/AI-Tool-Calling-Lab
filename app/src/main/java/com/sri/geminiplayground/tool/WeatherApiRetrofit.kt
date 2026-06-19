package com.sri.geminiplayground.tool

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiRetrofit {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: WeatherApi = retrofit.create(WeatherApi::class.java)
}