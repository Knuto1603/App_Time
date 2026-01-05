package com.example.weatherapp.data.remote.dto

data class OneCallResponse(
    val current: CurrentDto,
    val daily: List<DailyDto>
)

data class CurrentDto(
    val temp: Double
)

data class DailyDto(
    val dt: Long,
    val temp: TempDto,
    val feels_like: FeelsLikeDto,
    val wind_speed: Double,
    val uvi: Double,
    val weather: List<WeatherDto>
)

data class TempDto(
    val min: Double,
    val max: Double
)

data class FeelsLikeDto(
    val day: Double
)

data class WeatherDto(
    val description: String,
    val icon: String
)
