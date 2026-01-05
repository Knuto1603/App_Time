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
import com.example.weatherapp.ui.components.WeatherIcons.FEELS_LIKE
import com.example.weatherapp.ui.components.WeatherIcons.SKY
import com.example.weatherapp.ui.components.WeatherIcons.UV
import com.example.weatherapp.ui.components.WeatherIcons.WIND
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
                    val day = selected ?: days.getOrNull(2)

                    if (day != null) {

                        item {
                            Column(modifier = Modifier.padding(horizontal = 10.dp)) {

                                BentoCard(
                                    title = "Temperatura",
                                    value = "${(day.tempActual ?: day.tempAvg).toInt()}º",
                                    subtitle = "Máx ${day.tempMax.toInt()}º • Mín ${day.tempMin.toInt()}º",
                                    containerColor = Color(0xFFB8DAFF),
                                    isHighlight = true,
                                    iconCode = day.icon,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                )

                                Row(modifier = Modifier.fillMaxWidth()) {

                                    BentoCard(
                                        title = "Sensación",
                                        value = "${day.sensacion.toInt()}º",
                                        iconCode = FEELS_LIKE,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                    )

                                    BentoCard(
                                        title = "Viento",
                                        value = "${day.viento.toInt()} m/s",
                                        iconCode = WIND,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                    )
                                }

                                Row(modifier = Modifier.fillMaxWidth()) {

                                    BentoCard(
                                        title = "Cielo",
                                        value = day.descripcion.replaceFirstChar { it.uppercase() },
                                        iconCode = SKY,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                    )

                                    BentoCard(
                                        title = "Índice UV",
                                        value = day.uv.toInt().toString(),
                                        iconCode = UV,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                    )
                                }
                            }
                        }

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



