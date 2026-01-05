package com.example.weatherapp.data.remote

import com.example.weatherapp.data.remote.dto.OneCallResponse
import com.example.weatherapp.data.remote.dto.TimeMachineResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("onecall")
    suspend fun getOneCall(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es",
        @Query("appid") apiKey: String
    ): OneCallResponse

    @GET("onecall/timemachine")
    suspend fun getTimeMachine(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("dt") dt: Long,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es",
        @Query("appid") apiKey: String
    ): TimeMachineResponse
}