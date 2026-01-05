package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.remote.dto.DailyDto
import com.example.weatherapp.data.remote.dto.HourlyDto
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.utilities.toDate

object WeatherMapper {

    fun fromToday(
        daily: DailyDto,
        currentTemp: Double?
    ): DailyWeather {
        return buildFromDaily(
            daily = daily,
            tempActual = currentTemp
        )
    }

    fun fromForecast(daily: DailyDto): DailyWeather {
        return buildFromDaily(
            daily = daily,
            tempActual = null
        )
    }

    private fun buildFromDaily(
        daily: DailyDto,
        tempActual: Double?
    ): DailyWeather {

        val date = daily.dt.toDate()

        return DailyWeather(
            label = resolveLabel(date),
            icon = daily.weather.first().icon,
            date = date,
            tempActual = tempActual,
            tempMax = daily.temp.max,
            tempMin = daily.temp.min,
            tempAvg = (daily.temp.max + daily.temp.min) / 2,
            sensacion = daily.feels_like.day,
            uv = daily.uvi,
            viento = daily.wind_speed,
            descripcion = daily.weather.first().description
        )
    }

    fun fromHistory(
        hours: List<HourlyDto>,
        date: String
    ): DailyWeather {

        val temps = hours.map { it.temp }

        return DailyWeather(
            label = resolveLabel(date),
            icon = hours.first().weather.first().icon,
            date = date,
            tempActual = null,
            tempMax = temps.maxOrNull() ?: 0.0,
            tempMin = temps.minOrNull() ?: 0.0,
            tempAvg = temps.average(),
            sensacion = hours.map { it.feels_like }.average(),
            uv = hours.maxOfOrNull { it.uvi } ?: 0.0,
            viento = hours.map { it.wind_speed }.average(),
            descripcion = hours
                .flatMap { it.weather }
                .groupingBy { it.description }
                .eachCount()
                .maxByOrNull { it.value }?.key ?: ""
        )
    }


}

private fun resolveLabel(targetDate: String): String {

    val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

    val todayCal = java.util.Calendar.getInstance()
    zeroTime(todayCal)

    val targetCal = java.util.Calendar.getInstance()
    targetCal.time = format.parse(targetDate) ?: return targetDate
    zeroTime(targetCal)

    val diffMillis = targetCal.timeInMillis - todayCal.timeInMillis
    val diffDays = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diffMillis)

    return when (diffDays.toInt()) {
        -2 -> "Antes de ayer"
        -1 -> "Ayer"
        0 -> "Hoy"
        1 -> "Mañana"
        else -> targetDate
    }
}

private fun zeroTime(calendar: java.util.Calendar) {
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MILLISECOND, 0)
}


