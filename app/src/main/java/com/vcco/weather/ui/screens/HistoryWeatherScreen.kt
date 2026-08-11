package com.vcco.weather.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vcco.weather.R
import com.vcco.weather.model.weather.Clouds
import com.vcco.weather.model.weather.Conditions
import com.vcco.weather.model.weather.Coordinates
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.model.weather.Rain
import com.vcco.weather.model.weather.Snow
import com.vcco.weather.model.weather.SunTime
import com.vcco.weather.model.weather.Temperature
import com.vcco.weather.model.weather.Wind
import com.vcco.weather.ui.state.HistoryAppUiState
import java.util.Calendar
import java.util.Locale

@Composable
fun HistoryScreen(
    historyAppUiState: HistoryAppUiState,
    modifier: Modifier,
) {
    when (historyAppUiState) {
        is HistoryAppUiState.Loading -> {
            HistoryScreenLoading(modifier = modifier)
        }

        is HistoryAppUiState.Error -> {
            HistoryScreenError(
                errorMessage = historyAppUiState.error,
                modifier = modifier,
            )
        }

        is HistoryAppUiState.CompleteHistory -> {
            HistoryWeatherScreen(
                historySearch = historyAppUiState.weatherSearchList,
                modifier = modifier,
            )
        }

        is HistoryAppUiState.LastFive -> {
            HistoryWeatherScreen(
                historySearch = historyAppUiState.weatherSearchList,
                modifier = modifier,
            )
        }
    }
}

@Composable
fun HistoryScreenLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier =
                Modifier
                    .size(dimensionResource(R.dimen.size_128dp))
                    .testTag(stringResource(R.string.test_tag_loading))
                    .align(Alignment.Center),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun HistoryScreenError(
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(R.dimen.text_size_32_sp).value.sp,
            modifier =
                Modifier
                    .padding(dimensionResource(R.dimen.padding_8dp))
                    .align(Alignment.Center),
        )
    }
}

@Composable
fun HistoryWeatherScreen(
    historySearch: List<CurrentWeatherResponse>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
        modifier,
    ) {
        items(historySearch) { weather ->
            DetailWeatherScreen(
                currentWeatherResponse = weather,
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black,
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun HistoryWeatherLoadingPreview(){
    HistoryScreenLoading(
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun HistoryWeatherErrorPreview(){
    HistoryScreenError(
        errorMessage = stringResource(R.string.error_server_error),
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun HistoryWeatherScreenPreview(){
    HistoryWeatherScreen(
        listOf(
            CurrentWeatherResponse(
                coordinates =
                    Coordinates(
                        longitude = -96.9489f,
                        latitude = 32.814f,
                    ),
                conditions =
                    listOf(
                        Conditions(
                            id = 1,
                            condition = "clear",
                            description = "clear",
                            icon = "01d",
                        ),
                    ),
                base = "",
                temperature =
                    Temperature(
                        temperature = 90f,
                        feelsLike = 92f,
                        minTemperature = 68f,
                        maxTemperature = 95f,
                        humidity = 60,
                        pressure = 1012,
                        seaLevel = 1012,
                        groundLevel = 764,
                    ),
                visibility = 10000,
                wind =
                    Wind(
                        speed = 12.4f,
                        direction = 2,
                        gust = 14.4f,
                    ),
                clouds = Clouds(coverage = 10),
                rain = Rain(amount = 2.5f),
                snow = Snow(amount = 2.5f),
                dataCalculation = 1781378046,
                sunTime =
                    SunTime(
                        country = "US",
                        sunRiseTimestamp = 1727353131,
                        sunSetTimestamp = 1727396337,
                    ),
                timeZone = -18000,
                id = 4700168,
                name = "Irving",
            ),
        ), 
        modifier = Modifier.fillMaxSize()
    )
}