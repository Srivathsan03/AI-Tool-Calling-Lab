package com.sri.aitoolcallinglab.tool.weather

import com.sri.aitoolcallinglab.tool.Tool

class WeatherTool(
    private val weatherRepository: WeatherRepository
) : Tool {
    override val name = "weather"
    override val description =
        "Retrieves weather data for a given city. Expected format: city latitude longitude"

    override suspend fun execute(input: String?): String {
        val parts = input?.split(" ")
        val city = parts?.get(0)
        val latitude = parts?.get(1)?.toDoubleOrNull()
        val longitude = parts?.get(2)?.toDoubleOrNull()
        if (city == null || latitude == null || longitude == null) {
            return "Invalid input"
        }
        return weatherRepository.getWeather(city, latitude, longitude)
    }
}