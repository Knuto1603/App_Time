package com.example.weatherapp

import com.google.android.gms.maps.model.LatLng

object AppConfig {
    const val OPEN_WEATHER_API_KEY = "b082195d82cb5be0cd586112f4f30da2"
    const val BASE_URL = "https://api.openweathermap.org/data/3.0/"

    // Ubicación inicial (Madrid)
    val DEFAULT_LOCATION = LatLng(-5.1811154133849895, -80.61786739389342)
}