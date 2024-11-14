package com.example.weather.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherSearchScreen(
    uiState: WeatherUiState,
    onSearchCity: (String) -> Unit,
    stopLocation: () -> Unit,
) {
    var city by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(androidx.compose.material.MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        androidx.compose.material.Text(
            text = "Weather App",
            style = androidx.compose.material.MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp),
        )
        val keyboardController = LocalSoftwareKeyboardController.current
        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter city") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (city.isNotBlank()) {
                        keyboardController?.hide()
                        onSearchCity(city)
                    }
                },
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        val focusManager = LocalFocusManager.current

        Button(
            onClick = {
                if (city.isNotBlank()) {
                    focusManager.clearFocus()
                    stopLocation()
                    onSearchCity(city)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(text = "Search", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Show loading state
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error != null) {
            // Show error message
            Text(text = "Error: ${uiState.error}", color = Color.Red)
        } else {
            WeatherInfo(
                temperature = uiState.weather.temp,
                minTemp = uiState.weather.tempMin,
                maxTemp = uiState.weather.tempMax,
                feelsLike = uiState.weather.feelsLike,
                city = uiState.weather.city,
                weatherIcon = uiState.weather.weatherIcon,
            )
        }
    }
}


@Composable
fun WeatherInfo(
    temperature: Double,
    minTemp: Double,
    maxTemp: Double,
    feelsLike: Double,
    city: String,
    weatherIcon: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Weather icon
            Image(
                painter = rememberAsyncImagePainter(weatherIcon),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(100.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$temperature°F", fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "City: $city", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Min: $minTemp°F", fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Max: $maxTemp°F", fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Feels Like: $feelsLike°F", fontSize = 16.sp)
        }
    }
}
