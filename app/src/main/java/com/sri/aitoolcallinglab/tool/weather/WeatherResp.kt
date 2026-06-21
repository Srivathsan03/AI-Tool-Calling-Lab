package com.sri.aitoolcallinglab.tool.weather

import com.google.gson.annotations.SerializedName

data class WeatherResp(
    val current: Current
)

data class Current(
    @SerializedName("temperature_2m")
    val temperature: Double
)