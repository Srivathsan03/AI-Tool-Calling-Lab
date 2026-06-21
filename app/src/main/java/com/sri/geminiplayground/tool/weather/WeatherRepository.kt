package com.sri.geminiplayground.tool.weather

class WeatherRepository {

    suspend fun getWeather(city: String, latitude: Double, longitude: Double): String {
        val response = WeatherApiRetrofitProvider.api.getWeather(
            latitude = latitude,
            longitude = longitude
        )
        return """
            City: $city
            Temperature: ${response.current.temperature}°C
        """.trimIndent()
    }
}