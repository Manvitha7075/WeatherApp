package com.example.weather.di

import com.example.weather.data.repository.WeatherRepository
import com.example.weather.domain.usecase.GetWeatherByCityUseCase
import com.example.weather.domain.usecase.GetWeatherByLocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    /**
     * Provides an instance of GetWeatherByCityUseCase
     */
    @Provides
    @Singleton
    fun provideGetWeatherByCityUseCase(
        weatherRepository: WeatherRepository
    ): GetWeatherByCityUseCase {
        return GetWeatherByCityUseCase(weatherRepository)
    }

    /**
     * Provides an instance of GetWeatherByLocationUseCase
     */
    @Provides
    @Singleton
    fun provideGetWeatherByLocationUseCase(
        weatherRepository: WeatherRepository
    ): GetWeatherByLocationUseCase {
        return GetWeatherByLocationUseCase(weatherRepository)
    }

}