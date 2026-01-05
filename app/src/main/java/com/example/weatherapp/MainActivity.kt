package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.components.WeatherScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(color = Color.White) {

                    val viewModel: WeatherViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return WeatherViewModel(WeatherRepository()) as T
                            }
                        }
                    )


                    WeatherScreen(viewModel)
                }
            }
        }
    }
}
