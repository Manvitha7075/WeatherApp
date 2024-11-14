# Weather App Coding Challenge
## Overview
This is a weather application that allows users to search for weather data by city or by their current location. 
The app utilizes the OpenWeatherMap API to fetch weather information and displays it using a modern UI built with Jetpack Compose.

## Features
- Search for weather information by city name.
- Automatic retrieval of weather data based on the user's current location (with permission).
- Displays current weather conditions, temperature, humidity, and weather icons.
- Caches the last searched city for quick access.

## Technologies Used
- **Kotlin:** Programming language for Android development.
- **Jetpack Compose:** Modern UI toolkit for building native Android UI.
- **Hilt:** Dependency Injection library for managing dependencies.
- **Retrofit:** HTTP client for making API calls to the OpenWeatherMap service.
- **Coroutines:** For asynchronous programming.

## Installation Instructions
1. Clone the repository
2. Navigate to the project directory:
3. Open the project in Android Studio.
4. Sync the project with Gradle files to download dependencies. 
5. Run the application on an emulator or physical device.

## Usage
- Launch the application. 
- You can search for a city using the search bar. 
- If location permissions are granted, the app will automatically fetch weather data based on the user's current location.

### GetWeatherByCityUseCase
   Search Weather by City: Users can enter a city name to retrieve current weather data.
### GetWeatherByLocationUseCase
   Get Weather by Location:
   Upon granting location access, users will automatically see the weather data for their current location.

## Dependency Injection with Hilt
This project uses Hilt for dependency injection, ensuring a clean and maintainable codebase. Dependencies are provided through Hilt modules.

## API Calls with Retrofit
Retrofit is used for making network requests to the OpenWeatherMap API. All API calls are handled in a dedicated repository class.

## Testing
Unit tests have been implemented for the ViewModel and data layers to ensure functionality and reliability.


![SS1](https://github.com/user-attachments/assets/d2fecd9e-2905-4286-be77-ef1c5b6e5b5c)
![SS2](https://github.com/user-attachments/assets/f232cd1f-fa2b-4e24-8cee-d7c4d65a9bc2)
![SS3](https://github.com/user-attachments/assets/b5157146-a557-4d5b-823c-8bcf2f43f59d)



