package com.example.weatherapp.data.repository

import com.example.weatherapp.AppConfig
import com.example.weatherapp.data.remote.OpenWeatherApi
import com.example.weatherapp.data.mapper.WeatherMapper
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.remote.RetrofitClient
import com.example.weatherapp.data.utilities.toDate

class WeatherRepository(
    private val api: OpenWeatherApi = RetrofitClient.api,
    private val apiKey: String = AppConfig.OPEN_WEATHER_API_KEY
) {

    companion object {
        private const val SECONDS_IN_DAY = 86400L
    }

    suspend fun get4DaysWeather(
        lat: Double,
        lon: Double
    ): List<DailyWeather> {

        val oneCall = api.getOneCall(
            lat = lat,
            lon = lon,
            apiKey = apiKey
        )

        val today = WeatherMapper.fromToday(
            daily = oneCall.daily.first(),
            currentTemp = oneCall.current.temp
        )

        val tomorrow = WeatherMapper.fromForecast(
            daily = oneCall.daily[1]
        )

        // 🕰️ timestamps históricos
        val now = System.currentTimeMillis() / 1000

        val yesterdayResponse = api.getTimeMachine(
            lat = lat,
            lon = lon,
            dt = now - SECONDS_IN_DAY,
            apiKey = apiKey
        )

        val beforeYesterdayResponse = api.getTimeMachine(
            lat = lat,
            lon = lon,
            dt = now - (SECONDS_IN_DAY * 2),
            apiKey = apiKey
        )

        val yesterday = WeatherMapper.fromHistory(
            hours = yesterdayResponse.data,
            date = (now - SECONDS_IN_DAY).toDate()
        )

        val beforeYesterday = WeatherMapper.fromHistory(
            hours = beforeYesterdayResponse.data,
            date = (now - (SECONDS_IN_DAY * 2)).toDate()
        )

        // 📆 orden cronológico
        return listOf(
            beforeYesterday,
            yesterday,
            today,
            tomorrow
        )
    }
}
