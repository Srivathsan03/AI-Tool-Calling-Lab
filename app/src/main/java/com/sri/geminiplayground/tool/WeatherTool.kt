package com.sri.geminiplayground.tool

class WeatherTool(
    private val weatherRepository: WeatherRepository
) : Tool {
    override val name = "weather"

    override suspend fun execute(input: String?): String {
        val city = input?:"City Required"
        return weatherRepository.getWeather(city)
    }
}