package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import com.example.weatherapp.data.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.components.WeatherScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(color = Color.White) {

                    val viewModel: WeatherViewModel =
                        androidx.lifecycle.viewmodel.compose.viewModel()

                    WeatherScreen(viewModel)
                }
            }
        }
    }
}
