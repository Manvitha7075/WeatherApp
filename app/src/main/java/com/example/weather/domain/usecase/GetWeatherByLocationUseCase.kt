package com.example.weather.domain.usecase

import com.example.weather.data.repository.WeatherRepository
import com.example.weather.domain.model.WeatherDomain
import javax.inject.Inject

class GetWeatherByLocationUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    /**
     * Executes the use case to fetch weather data based on geographical coordinates latitude and longitude
     */
    suspend operator fun invoke(lat: Double, lon: Double): Result<WeatherDomain> {
        return weatherRepository.getWeatherDataByLocation(lat, lon)
    }
}