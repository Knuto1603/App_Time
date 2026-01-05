package com.example.weatherapp.data.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.toDate(): String {
    val date = Date(this * 1000) // seconds → millis
    val format = SimpleDateFormat("dd/MM", Locale.getDefault())
    format.timeZone = TimeZone.getDefault()
    return format.format(date)
}
