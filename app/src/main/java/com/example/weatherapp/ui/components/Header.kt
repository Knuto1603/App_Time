package com.example.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppHeader(title: String, showBack: Boolean, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Black)
            Text(text = "Clima en tiempo real", fontSize = 12.sp, color = Color.Gray)
        }
        if (showBack) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.Black, RoundedCornerShape(12.dp))
            ) {
                Text("←", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
