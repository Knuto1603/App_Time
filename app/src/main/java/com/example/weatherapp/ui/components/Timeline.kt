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
import com.example.weatherapp.data.model.DailyWeather

@Composable
fun TimelineSection(
    days: List<DailyWeather>,
    onDaySelected: (DailyWeather) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Historial y Pronóstico",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(days.size) { index ->
                val day = days[index]

                TimelineItem(
                    label = day.label,
                    temp = "${day.tempAvg.toInt()}º",
                    iconCode = day.icon,
                    isActive = true
                ) {
                    onDaySelected(day)
                }
            }
        }
    }
}

@Composable
fun TimelineItem(
    label: String,
    temp: String,
    iconCode: String?,
    isActive: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(125.dp)
            .clickable(enabled = isActive, onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFFF1F1F1) else Color(0xFFF8F8F8)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = label,
                fontSize = 14.sp,
                color = if (isActive) Color.Gray else Color.LightGray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                iconCode?.let {
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/${it}@4x.png",
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Text(
                    text = temp,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Color.Black else Color.LightGray
                )

            }


        }
    }
}
