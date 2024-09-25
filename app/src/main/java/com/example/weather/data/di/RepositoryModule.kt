package com.example.weather.data.di

import com.example.weather.data.api.WeatherApiService
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.data.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides an instance of WeatherRepository
     */
    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherApiService: WeatherApiService,
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherApiService)
    }
}