package com.sri.geminiplayground.tool

data class WeatherResp(
    val current: Current
)

data class Current(
    val temperature_2m: Double
)