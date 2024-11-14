package com.example.weather.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.model.WeatherDomain
import com.example.weather.domain.usecase.GetWeatherByCityUseCase
import com.example.weather.domain.usecase.GetWeatherByLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing weather data and UI state
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    // StateFlow to hold the UI state for weather data
    private val _weatherUiState = MutableStateFlow(WeatherUiState())
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    // StateFlow to hold the last searched city
    private val _lastSearchedCity = MutableStateFlow<String>(getLastSearchedCity())
    val lastSearchedCity: StateFlow<String> = _lastSearchedCity.asStateFlow()

    /**
     * Function to fetch weather data based on the city name
     */
    fun getWeatherData(city: String) {
        viewModelScope.launch {
            // Update state to loading
            _weatherUiState.value = WeatherUiState(isLoading = true)
            saveLastSearchedCity(city)
            getWeatherByCityUseCase(city)
                .onSuccess { weatherDomain ->
                    // Update UI state with the received data
                    _weatherUiState.value = WeatherUiState(isLoading = false)
                    _weatherUiState.value = WeatherUiState(weather = weatherDomain)
                }
                .onFailure { exception ->
                    _weatherUiState.value = WeatherUiState(isLoading = false)
                    _weatherUiState.value = WeatherUiState(error = exception.message)
                }
        }
    }

    /**
     * Function to fetch weather data based on geographical coordinates latitude and longitude
     */
    fun getWeatherDataByLocation(lat: Double, lon: Double) {
        _weatherUiState.value = WeatherUiState(isLoading = true)
        viewModelScope.launch {
            getWeatherByLocationUseCase(lat, lon)
                .onSuccess { weatherDomain ->
                    _weatherUiState.value = WeatherUiState(isLoading = false)
                    _weatherUiState.value = WeatherUiState(weather = weatherDomain)
                }
                .onFailure { exception ->
                    _weatherUiState.value = WeatherUiState(isLoading = false)
                    _weatherUiState.value = WeatherUiState(error = exception.message)
                }
        }
    }

    /**
     * Save the last searched city in SharedPreferences
     */
    private fun saveLastSearchedCity(city: String) {
        sharedPreferences.edit().putString("last_city", city).apply()
    }

    /**
     * Get the last searched city from SharedPreferences
     */
    private fun getLastSearchedCity(): String {
        return sharedPreferences.getString("last_city", "")!!
    }
}

// Data class representing the UI state for weather data
data class WeatherUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val weather: WeatherDomain = WeatherDomain(),
)
