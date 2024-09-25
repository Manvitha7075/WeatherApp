package com.example.weather.domain.usecase

import com.example.weather.data.repository.WeatherRepository
import com.example.weather.domain.model.WeatherDomain
import javax.inject.Inject

class GetWeatherByCityUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    /**
     * Executes the use case to fetch weather data for the specified city
     */
    suspend operator fun invoke(city: String): Result<WeatherDomain> {
        return weatherRepository.getWeatherData(city)
    }
}