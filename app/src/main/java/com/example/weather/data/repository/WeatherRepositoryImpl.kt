package com.example.weather.data.repository

import com.example.weather.data.api.WeatherApiService
import com.example.weather.data.model.WeatherResponse
import com.example.weather.domain.model.WeatherDomain
import javax.inject.Inject

/**
 * Implementation of the WeatherRepository interface that interacts with the WeatherApiService to fetch weather data.
 */
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
) : WeatherRepository {
    /**
     * Fetch weather data for a specific city
     */
    override suspend fun getWeatherData(city: String): Result<WeatherDomain> {
        return try {
            val response = weatherApiService.getWeatherByCity(city, unit, apiKey)
            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    val weatherDomain = weatherResponse.toDomain()
                    Result.success(weatherDomain)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("Error fetching weather data: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetch weather data by geographical coordinates latitude and longitude
     */
    override suspend fun getWeatherDataByLocation(
        lat: Double,
        lon: Double,
    ): Result<WeatherDomain> {
        return try {
            val response = weatherApiService.getWeatherByLocation(lat, lon, unit, apiKey)
            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    val weatherDomain = weatherResponse.toDomain()
                    Result.success(weatherDomain)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                // Handle unsuccessful response
                Result.failure(Exception("Error fetching weather data: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get the full URL for the weather icon using the iconcode from the API response
     */
    fun getWeatherIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/wn/$iconCode@2x.png"
    }

    /**
     * Helper function to convert the API response into the domain model
     */
    private fun WeatherResponse.toDomain(): WeatherDomain {
        return WeatherDomain(
            lon = coord.lon,
            lat = coord.lat,
            tempMin = main.tempMin,
            tempMax = main.tempMax,
            temp = main.temp,
            feelsLike = main.feelsLike,
            main = weather.first().main,
            country = sys.country,
            city = name,
            weatherIcon = getWeatherIconUrl(weather.first().icon),
        )
    }

    /**
     * Companion object to hold constant values API key and unit
     */
    companion object {
        const val apiKey = "7fda0100ab024247254a8f0b29ff3e1e"
        const val unit = "imperial"
    }
}
