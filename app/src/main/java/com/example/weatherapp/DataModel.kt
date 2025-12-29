package com.example.weatherapp

import com.google.gson.annotations.SerializedName

/**
 * WeatherResponse: Respuesta para el clima actual y pronóstico (One Call 3.0).
 */
data class WeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val current: WeatherCurrentData,
    val daily: List<DailyWeather>? = null,
    val alerts: List<Alert>? = null
)

/**
 * HistoryResponse: Respuesta para el historial (Time Machine).
 */
data class HistoryResponse(
    val lat: Double,
    val lon: Double,
    val data: List<WeatherHistoryData> // Usa el modelo de historial
)

/**
 * Datos para el Clima Actual.
 */
data class WeatherCurrentData(
    val dt: Long,
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val uvi: Double,
    @SerializedName("wind_speed") val windSpeed: Double,
    val weather: List<WeatherDescription>
)

/**
 * Datos para el Historial (Time Machine).
 * IMPORTANTE: Aquí temp y feels_like son Double, no objetos.
 */
data class WeatherHistoryData(
    val dt: Long,
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val uvi: Double? = 0.0,
    @SerializedName("wind_speed") val windSpeed: Double,
    val weather: List<WeatherDescription>
)

/**
 * Datos para el Pronóstico Diario (Mañana).
 * Aquí temp y feels_like SÍ son objetos (TempDetails/Feelslike).
 */
data class DailyWeather(
    val dt: Long,
    val temp: TempDetails,
    val humidity: Int,
    val pressure: Int,
    val uvi: Double,
    val windSpeed: Double,
    @SerializedName("feels_like") val feelsLike: Feelslike,
    val weather: List<WeatherDescription>
)

data class Alert(
    @SerializedName("sender_name") val senderName: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String
)

data class TempDetails(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Feelslike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class WeatherDescription(
    val description: String,
    val icon: String
)