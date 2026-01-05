package com.example.weatherapp.data.model

data class DailyWeather(


    val label: String,
    val date: String,

    val tempMin: Double,
    val tempMax: Double,
    val tempAvg: Double,
    val tempActual: Double? = null,

    val sensacion: Double,

    val viento: Double,

    val uv: Double,

    val descripcion: String,
    val icon: String
)
