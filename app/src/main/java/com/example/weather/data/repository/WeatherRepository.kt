package com.example.weather.data.repository

import com.example.weather.domain.model.WeatherDomain

interface WeatherRepository {
    /**
     * Function to fetch weather data based on a city name
     */
    suspend fun getWeatherData(city: String): Result<WeatherDomain>

    /**
     * Function to fetch weather data based on geographical coordinates (latitude and longitude)
     */
    suspend fun getWeatherDataByLocation(lat: Double, lon: Double): Result<WeatherDomain>
}
