package com.example.weatherapp

import com.example.weatherapp.AppConfig
import com.example.weatherapp.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * WeatherApiService define el contrato de la API.
 */
interface WeatherApiService {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es",
        @Query("exclude") exclude: String = "minutely,hourly,dayly"
    ): WeatherResponse
    @GET("onecall/timemachine")
    suspend fun getHistory(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("dt") dt: Long,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es",
        @Query("exclude") exclude: String = "current,minutely,hourly"
    ): HistoryResponse
}

/**
 * Cliente Retrofit para la comunicacion HHTTPS
 */
object RetrofitClient {
    val service: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}