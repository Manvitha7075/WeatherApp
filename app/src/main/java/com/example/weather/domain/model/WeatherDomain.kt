package com.example.weather.domain.model

/**
 * Data class representing weather information
 */
data class WeatherDomain(
    val lon: Double = 0.0,
    val lat: Double = 0.0,
    val city: String = "",
    val tempMin: Double = 0.0,
    val tempMax: Double = 0.0,
    val temp: Double = 0.0,
    val feelsLike: Double = 0.0,
    val main: String = "",
    val country: String = "",
    val weatherIcon: String = "",
)
