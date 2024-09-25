package com.example.weather

import android.content.SharedPreferences
import com.example.weather.domain.model.WeatherDomain
import com.example.weather.domain.usecase.GetWeatherByCityUseCase
import com.example.weather.domain.usecase.GetWeatherByLocationUseCase
import com.example.weather.ui.WeatherViewModel
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    private lateinit var weatherViewModel: WeatherViewModel

    @Mock
    private lateinit var getWeatherByCityUseCase: GetWeatherByCityUseCase

    @Mock
    private lateinit var getWeatherByLocationUseCase: GetWeatherByLocationUseCase

    private val sharedPreferences: SharedPreferences = mock(SharedPreferences::class.java)
    private val editor: SharedPreferences.Editor = mock(SharedPreferences.Editor::class.java)

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        `when`(sharedPreferences.getString("last_city", "")).thenReturn("")
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        weatherViewModel = WeatherViewModel(getWeatherByCityUseCase,getWeatherByLocationUseCase,sharedPreferences)
    }

    @Test
    fun `getWeatherData should update UI state on success`() = runBlockingTest {
        val city = "Rochester"
        val weatherDomain = WeatherDomain(
            lon = 0.1278,
            lat = 51.5074,
            city = "Rochester",
            tempMin = 10.0,
            tempMax = 20.0,
            temp = 15.0,
            feelsLike = 14.0,
        )

        `when`(getWeatherByCityUseCase(city)).thenReturn(Result.success(weatherDomain))
        weatherViewModel.getWeatherData(city)

        assert(weatherViewModel.weatherUiState.value.weather == weatherDomain)
        verify(sharedPreferences).edit()
        verify(sharedPreferences.edit()).putString("last_city", city)
        verify(sharedPreferences.edit()).apply()
    }

    @Test
    fun `getWeatherData should update UI state on failure`() = runBlockingTest {
        val city = "Rochester"
        val errorMessage = "Unable to get data"

        `when`(getWeatherByCityUseCase(city)).thenReturn(
            Result.failure(
                Exception(
                    errorMessage,
                ),
            ),
        )
        weatherViewModel.getWeatherData(city)
        advanceUntilIdle()
        assert(weatherViewModel.weatherUiState.value.error == errorMessage) // Check error message
    }

    @Test
    fun `getWeatherDataByLocation should update UI state on success`() = runBlockingTest {
        val lat = 51.5074
        val lon = -0.1278
        val weatherDomain = WeatherDomain(
            lon = lon,
            lat = lat,
            city = "Rochester",
            tempMin = 10.0,
            tempMax = 20.0,
            temp = 15.0,
            feelsLike = 14.0,
        )

        `when`(getWeatherByLocationUseCase(lat, lon)).thenReturn(
            Result.success(
                weatherDomain,
            ),
        )
        weatherViewModel.getWeatherDataByLocation(lat, lon)
        advanceUntilIdle()
        assert(weatherViewModel.weatherUiState.value.weather == weatherDomain)
    }

    @Test
    fun `getWeatherDataByLocation should update UI state on failure`() = runBlockingTest {
        val lat = 51.5074
        val lon = -0.1278
        val errorMessage = "Unable to get data"

        `when`(getWeatherByLocationUseCase(lat, lon)).thenReturn(
            Result.failure(
                Exception(errorMessage),
            ),
        )
        weatherViewModel.getWeatherDataByLocation(lat, lon)
        advanceUntilIdle()
        assert(weatherViewModel.weatherUiState.value.error == errorMessage)
    }

    @Test
    fun `toGetWeatherData should update UI state on failure`() = runBlockingTest {
        val errorMessage = "Unable to fetch data"
        val city = "Rochester"

        `when`(getWeatherByCityUseCase(city)).thenReturn(
            Result.failure(
                Exception(
                    errorMessage,
                ),
            ),
        )

        weatherViewModel.getWeatherData(city)
        advanceUntilIdle()
        val currentState = weatherViewModel.weatherUiState.value
        println(currentState)

        assertEquals(errorMessage, currentState.error) // Check error message matches
    }
}
