package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherapp.AppConfig
import com.example.weatherapp.data.viewmodel.WeatherUiState
import com.example.weatherapp.data.viewmodel.WeatherViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val selected = viewModel.selectedReport

    var currentLocation by remember { mutableStateOf(AppConfig.DEFAULT_LOCATION) }

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchFullData(currentLocation)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        AppHeader(
            title = if (viewModel.isShowingCurrent)
                "AppWeather"
            else
                "Día: ${selected?.date ?: ""}",
            showBack = !viewModel.isShowingCurrent,
            onBack = {
                if (uiState is WeatherUiState.Success) {
                    viewModel.selectCurrent(
                        (uiState as WeatherUiState.Success).fullData
                    )
                }
            }
        )

        // MAPA
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp),
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

                is WeatherUiState.Loading -> {
                    item { LoadingBox() }
                }

                is WeatherUiState.Error -> {
                    item { ErrorBox((uiState as WeatherUiState.Error).message) }
                }

                is WeatherUiState.Success -> {

                    val days = (uiState as WeatherUiState.Success).fullData
                    val day = selected ?: days.getOrNull(2) // HOY por defecto

                    if (day != null) {

                        item {
                            Column(modifier = Modifier.padding(horizontal = 10.dp)) {

                                // 🌡️ TEMPERATURA PRINCIPAL
                                BentoCard(
                                    title = "Temperatura",
                                    value = "${(day.tempActual ?: day.tempAvg).toInt()}º",
                                    subtitle = "Máx ${day.tempMax.toInt()}º • Mín ${day.tempMin.toInt()}º",
                                    containerColor = Color(0xFFE3F2FD),
                                    isHighlight = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                )

                                Row(modifier = Modifier.fillMaxWidth()) {

                                    BentoCard(
                                        title = "Sensación",
                                        value = "${day.sensacion.toInt()}º",
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                    )

                                    BentoCard(
                                        title = "Viento",
                                        value = "${day.viento.toInt()} m/s",
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                    )
                                }

                                Row(modifier = Modifier.fillMaxWidth()) {

                                    BentoCard(
                                        title = "Cielo",
                                        value = day.descripcion.replaceFirstChar { it.uppercase() },
                                        containerColor = Color(0xFFF1F1F1),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                    )

                                    BentoCard(
                                        title = "Índice UV",
                                        value = day.uv.toInt().toString(),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                    )
                                }
                            }
                        }

                        // 📆 TIMELINE
                        item {
                            TimelineSection(
                                days = days,
                                onDaySelected = { viewModel.selectDay(it) }
                            )
                        }
                    }
                }
            }

            item { AppFooter() }
        }
    }
}



