package com.example.weatherapp.data.remote.dto

data class TimeMachineResponse(
    val data: List<HourlyDto>
)

data class HourlyDto(
    val temp: Double,
    val feels_like: Double,
    val wind_speed: Double,
    val uvi: Double,
    val weather: List<WeatherDto>
)

