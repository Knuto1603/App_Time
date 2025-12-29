package com.example.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherapp.AppConfig
import com.example.weatherapp.WeatherUiState
import com.example.weatherapp.WeatherViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val uiState = viewModel.uiState
    var currentLocation by remember { mutableStateOf(AppConfig.DEFAULT_LOCATION) }
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchFullData(currentLocation)
    }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        AppHeader(
            title = if (viewModel.isShowingCurrent) "BentoWeather" else "Día: ${viewModel.selectedReport?.label}",
            showBack = !viewModel.isShowingCurrent,
            onBack = {
                if (uiState is WeatherUiState.Success) viewModel.selectCurrent(uiState.fullData)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth().height(220.dp).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(32.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraState,
                onMapClick = {
                    currentLocation = it
                    viewModel.fetchFullData(it)
                }
            ) {
                Marker(state = MarkerState(position = currentLocation))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            when (uiState) {
                is WeatherUiState.Loading -> item { LoadingBox() }
                is WeatherUiState.Error -> item { ErrorBox(uiState.message) }
                is WeatherUiState.Success -> {
                    val report = viewModel.selectedReport
                    if (report != null) {
                        items(items = report.alerts) { alert ->
                            AlertCard(event = alert.event, description = alert.description)
                        }

                        item {
                            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                BentoCard(
                                    title = "Temperatura",
                                    value = "${report.tempDisplay.toInt()}º",
                                    subtitle = "Máx: ${report.tempMax.toInt()}º • Mín: ${report.tempMin.toInt()}º",
                                    iconCode = report.icon,
                                    containerColor = Color(0xFFE3F2FD),
                                    isHighlight = true,
                                    modifier = Modifier.fillMaxWidth().height(160.dp)
                                )
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    BentoCard(
                                        title = "Humedad",
                                        value = "${report.humidity}%",
                                        iconCode = "https://img.icons8.com/ios-glyphs/60/737373/humidity.png",
                                        modifier = Modifier.weight(1f).height(120.dp)
                                    )
                                    BentoCard(
                                        title = "Viento",
                                        value = "${report.windSpeed.toInt()} m/s",
                                        iconCode = "https://img.icons8.com/ios-glyphs/60/737373/wind.png",
                                        modifier = Modifier.weight(1f).height(120.dp)
                                    )
                                }
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    BentoCard(
                                        title = "Cielo",
                                        value = report.description.replaceFirstChar { it.uppercase() },
                                        containerColor = Color(0xFFF1F1F1),
                                        modifier = Modifier.weight(1f).height(120.dp)
                                    )
                                    BentoCard(
                                        title = "Índice UV",
                                        value = "${report.uvi.toInt()}",
                                        iconCode = "https://img.icons8.com/ios-glyphs/60/737373/sun--v1.png",
                                        modifier = Modifier.weight(1f).height(120.dp)
                                    )
                                }
                            }
                        }

                        item { TimelineSection(uiState.fullData, viewModel) }
                    }
                }
            }
            item { AppFooter() }
        }
    }
}


