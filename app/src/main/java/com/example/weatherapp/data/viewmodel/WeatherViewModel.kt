package com.example.weatherapp.data.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.model.DailyWeather
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    var selectedReport by mutableStateOf<DailyWeather?>(null)
        private set

    var isShowingCurrent by mutableStateOf(true)
        private set

    /**
     * Llama al backend (repository)
     */

    fun fetchFullData(location: LatLng) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val data = repository.get4DaysWeather(
                    lat = location.latitude,
                    lon = location.longitude
                )

                selectedReport = data.getOrNull(2) // HOY
                isShowingCurrent = true

                _uiState.value = WeatherUiState.Success(data)

            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    e.message ?: "Error al obtener clima"
                )
            }
        }
    }

    /**
     * Vuelve a HOY
     */
    fun selectCurrent(fullData: List<DailyWeather>) {
        selectedReport = fullData.getOrNull(2)
        isShowingCurrent = true
    }

    /**
     * Selecciona cualquier día (ayer / mañana)
     */
    fun selectDay(day: DailyWeather) {
        selectedReport = day
        isShowingCurrent = false
    }
}


sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val fullData: List<DailyWeather>) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
