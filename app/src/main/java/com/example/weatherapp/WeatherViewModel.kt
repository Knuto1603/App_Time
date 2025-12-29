package com.example.weatherapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var uiState by mutableStateOf<WeatherUiState>(WeatherUiState.Loading)
        private set

    var selectedReport by mutableStateOf<DisplayableWeather?>(null)
    var isShowingCurrent by mutableStateOf(true)

    fun fetchFullData(coords: LatLng) {
        viewModelScope.launch {
            uiState = WeatherUiState.Loading
            try {
                val now = System.currentTimeMillis() / 1000
                val day1Ago = now - 86400
                val day2Ago = now - 172800

                val currentDef = async { RetrofitClient.service.getWeather(coords.latitude, coords.longitude, AppConfig.OPEN_WEATHER_API_KEY) }
                val h1Def = async { RetrofitClient.service.getHistory(coords.latitude, coords.longitude, day1Ago, AppConfig.OPEN_WEATHER_API_KEY) }
                val h2Def = async { RetrofitClient.service.getHistory(coords.latitude, coords.longitude, day2Ago, AppConfig.OPEN_WEATHER_API_KEY) }

                val currentRes = currentDef.await()
                val h1Res = h1Def.await()
                val h2Res = h2Def.await()

                val dataSet = FullWeatherData(
                    current = currentRes,
                    history1 = h1Res.data[0],
                    history2 = h2Res.data[0]
                )

                uiState = WeatherUiState.Success(dataSet)
                selectCurrent(dataSet)
            } catch (e: Exception) {
                uiState = WeatherUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun selectCurrent(fullData: FullWeatherData) {
        val data = fullData.current.current
        val todayStats = fullData.current.daily?.firstOrNull()?.temp

        selectedReport = DisplayableWeather(
            label = "Actual",
            tempDisplay = data.temp,
            tempMin = todayStats?.min ?: data.temp,
            tempMax = todayStats?.max ?: data.temp,
            humidity = data.humidity,
            windSpeed = data.windSpeed,
            uvi = data.uvi,
            pressure = data.pressure,
            icon = data.weather.firstOrNull()?.icon,
            description = data.weather.firstOrNull()?.description ?: "",
            alerts = fullData.current.alerts ?: emptyList()
        )
        isShowingCurrent = true
    }

    fun selectTomorrow(fullData: FullWeatherData) {
        val tomorrow = fullData.current.daily?.getOrNull(1)
        tomorrow?.let {
            selectedReport = DisplayableWeather(
                label = "Mañana",
                tempDisplay = it.temp.day,
                tempMin = it.temp.min,
                tempMax = it.temp.max,
                humidity = it.humidity,
                windSpeed = it.windSpeed,
                uvi = it.uvi,
                pressure = it.pressure,
                icon = it.weather.firstOrNull()?.icon,
                description = it.weather.firstOrNull()?.description ?: "",
                alerts = emptyList()
            )
            isShowingCurrent = false
        }
    }

    fun selectHistory(label: String, data: WeatherHistoryData) {
        selectedReport = DisplayableWeather(
            label = label,
            tempDisplay = data.temp,
            tempMin = data.temp,
            tempMax = data.temp,
            humidity = data.humidity,
            windSpeed = data.windSpeed,
            uvi = data.uvi ?: 0.0,
            pressure = data.pressure,
            icon = data.weather.firstOrNull()?.icon,
            description = data.weather.firstOrNull()?.description ?: "",
            alerts = emptyList()
        )
        isShowingCurrent = false
    }
}

data class FullWeatherData(
    val current: WeatherResponse,
    val history1: WeatherHistoryData,
    val history2: WeatherHistoryData
)

data class DisplayableWeather(
    val label: String,
    val tempDisplay: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val windSpeed: Double,
    val uvi: Double,
    val pressure: Int,
    val icon: String?,
    val description: String,
    val alerts: List<Alert>
)

sealed interface WeatherUiState {
    object Loading : WeatherUiState
    data class Success(val fullData: FullWeatherData) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}