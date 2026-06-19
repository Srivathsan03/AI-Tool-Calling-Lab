package com.sri.geminiplayground.tool

class WeatherRepository {

    suspend fun getWeather(city: String): String {
        val response = WeatherApiRetrofit.api.getWeather(
            latitude = 13.0878,
            longitude = 80.2785
        )
        return """
            City: $city
            Temperature: ${response.current.temperature}°C
        """.trimIndent()
    }
}