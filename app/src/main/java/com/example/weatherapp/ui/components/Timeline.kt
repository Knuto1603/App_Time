package com.example.weatherapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.FullWeatherData
import com.example.weatherapp.WeatherViewModel

@Composable
fun TimelineSection(fullData: FullWeatherData, viewModel: WeatherViewModel) {
    // Para mañana usamos el bloque daily[1] del reporte actual, no una petición de historial
    val tomorrowData = fullData.current.daily?.getOrNull(1)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Historial y Pronóstico", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Hace 2 días
            item {
                TimelineItem(
                    label = "Hace 2 días",
                    temp = "${fullData.history2.temp.toInt()}ºC",
                    iconCode = fullData.history2.weather.firstOrNull()?.icon
                ) {
                    viewModel.selectHistory("Hace 2 días", fullData.history2)
                }
            }
            // Ayer
            item {
                TimelineItem(
                    label = "Ayer",
                    temp = "${fullData.history1.temp.toInt()}ºC",
                    iconCode = fullData.history1.weather.firstOrNull()?.icon
                ) {
                    viewModel.selectHistory("Ayer", fullData.history1)
                }
            }
            // Mañana (desde Pronóstico Diario)
            item {
                val tempTomorrow = tomorrowData?.temp?.day?.toString() ?: "--"
                TimelineItem(
                    label = "Mañana",
                    temp = "${tempTomorrow}ºC",
                    iconCode = tomorrowData?.weather?.firstOrNull()?.icon,
                    isActive = tomorrowData != null
                ) {
                    viewModel.selectTomorrow(fullData)
                }
            }
        }
    }
}

@Composable
fun TimelineItem(
    label: String,
    temp: String,
    iconCode: String? = null,
    isActive: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable(enabled = isActive) { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFFF1F1F1) else Color(0xFFF8F8F8)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 11.sp, color = if(isActive) Color.Gray else Color.LightGray)

            Spacer(modifier = Modifier.height(4.dp))

            if (iconCode != null) {
                val iconUrl = if (iconCode.startsWith("http")) iconCode
                else "https://openweathermap.org/img/wn/$iconCode@4x.png"

                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = temp,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if(isActive) Color.Black else Color.LightGray
            )
        }
    }
}
