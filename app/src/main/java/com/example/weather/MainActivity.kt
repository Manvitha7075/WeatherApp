package com.example.weather

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weather.data.location.LocationHelper
import com.example.weather.ui.WeatherSearchScreen
import com.example.weather.ui.WeatherViewModel
import com.example.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var locationHelper: LocationHelper
    private lateinit var weatherViewModel: WeatherViewModel


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            locationHelper.checkLocationPermission()
        } else {
            fetchWeatherDataWithLastSearchedCity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            weatherViewModel = hiltViewModel()
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    WeatherSearchScreen(
                        uiState = weatherViewModel.weatherUiState.collectAsState().value,
                        onSearchCity = { city ->
                            weatherViewModel.getWeatherData(city)
                        },
                        stopLocation = { locationHelper.stopLocationUpdates() },
                    )
                }
            }

            locationHelper = LocationHelper(this, requestPermissionLauncher) { location ->
                getWeatherDataFromLocation(location)
            }
            locationHelper.checkLocationPermission()
        }
    }

    /**
     * Function to get the weather data based on the current location
     */
    private fun getWeatherDataFromLocation(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        locationHelper.stopLocationUpdates()
        weatherViewModel.getWeatherDataByLocation(latitude, longitude)
    }

    /**
     * Function to fetch weather data using the last searched city stored in shared preferences
     */
    private fun fetchWeatherDataWithLastSearchedCity() {
        val lastSearchedCity = weatherViewModel.lastSearchedCity.value
        if (!lastSearchedCity.isNullOrEmpty()) {
            weatherViewModel.getWeatherData(lastSearchedCity)
        }
    }
}