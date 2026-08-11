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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.vcco.weather.ui.state.DetailAppUiState
import java.util.Calendar
import java.util.Locale

/**
 * Show Detailed weather screen according to [detailAppUiState]
 * Showing error message in case of error
 * and info in case of success
 */
@Composable
fun DetailScreen(
    detailAppUiState: DetailAppUiState,
    modifier: Modifier = Modifier,
) {
    when (detailAppUiState) {
        is DetailAppUiState.Loading -> {
            DetailScreenLoading(modifier = modifier)
        }

        is DetailAppUiState.Error -> {
            DetailScreenError(
                errorMessage = detailAppUiState.error,
                modifier = modifier,
            )
        }

        is DetailAppUiState.Success -> {
            DetailWeatherScreen(
                currentWeatherResponse = detailAppUiState.currentWeatherResponse,
                modifier = modifier,
            )
        }
    }
}

/**
 * Show loading state
 */
@Composable
fun DetailScreenLoading(modifier: Modifier = Modifier) {
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

/**
 * Show message error
 */
@Composable
fun DetailScreenError(
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

/**
 * Show the weather for the current search
 */
@Composable
fun DetailWeatherScreen(
    currentWeatherResponse: CurrentWeatherResponse,
    modifier: Modifier = Modifier,
) {
    val currentConditions = currentWeatherResponse.conditions.first()
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_16dp)),
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = currentWeatherResponse.name,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.text_size_32_sp).value.sp,
                modifier =
                    Modifier
                        .padding(dimensionResource(R.dimen.padding_8dp))
                        .align(Alignment.CenterHorizontally),
            )

            Row(
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally),
            ) {
                Image(
                    painter =
                        painterResource(
                            convertURLToDrawableId(
                                currentConditions.icon,
                            ),
                        ),
                    contentDescription = currentConditions.description,
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = currentConditions.condition,
                    modifier =
                        Modifier
                            .weight(1f)
                            .align(alignment = Alignment.CenterVertically),
                )
            }
            Text(
                text = stringResource(R.string.label_current_temperature),
                modifier =
                    Modifier
                        .align(alignment = Alignment.CenterHorizontally),
            )

            Row(
                modifier =
                    Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(dimensionResource(R.dimen.padding_64dp)),
            ) {
                Text(
                    text =
                        stringResource(
                            R.string.label_current_temperature_format,
                            currentWeatherResponse.temperature.temperature.toInt(),
                        ),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(R.dimen.text_size_64_sp).value.sp,
                    modifier =
                        Modifier
                            .align(alignment = Alignment.CenterVertically),
                )
            }
            Column {
                Text(
                    text =
                        stringResource(
                            R.string.label_temperature_real_feel,
                            currentWeatherResponse.temperature.feelsLike.toInt(),
                        ),
                    modifier =
                        Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(dimensionResource(R.dimen.padding_16dp)),
                )
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        text =
                            stringResource(
                                R.string.label_temperature_min,
                                currentWeatherResponse.temperature.minTemperature.toInt(),
                            ),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Light,
                        modifier =
                            Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(end = dimensionResource(R.dimen.padding_8dp))
                                .weight(1f),
                    )
                    Text(
                        text =
                            stringResource(
                                R.string.label_temperature_max,
                                currentWeatherResponse.temperature.maxTemperature.toInt(),
                            ),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Light,
                        modifier =
                            Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(start = dimensionResource(R.dimen.padding_8dp))
                                .weight(1f),
                    )
                }
            }
            currentWeatherResponse.temperature.pressure?.let {
                Text(
                    text =
                        stringResource(
                            R.string.label_current_pressure,
                            it,
                        ),
                    modifier =
                        Modifier
                            .align(alignment = Alignment.CenterHorizontally),
                )
            }
            val humidityModifier = if (currentWeatherResponse.temperature.pressure == null)
                Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(dimensionResource(R.dimen.padding_16dp))
            else {
                Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = dimensionResource(R.dimen.padding_16dp),
                        start = dimensionResource(R.dimen.padding_16dp),
                        end = dimensionResource(R.dimen.padding_16dp))
            }

            Text(
                text =
                    stringResource(
                        R.string.label_current_humidity,
                        currentWeatherResponse.temperature.humidity,
                    ),
                modifier = humidityModifier,
            )

            Text(
                text =
                    stringResource(
                        R.string.label_cloud_coverage,
                        currentWeatherResponse.clouds.coverage,
                    ),
                modifier =
                    Modifier
                        .align(alignment = Alignment.CenterHorizontally),
            )

            val precipitation =
                when {
                    currentWeatherResponse.rain != null -> {
                        stringResource(
                            R.string.label_current_rain_expected,
                            currentWeatherResponse.rain.amount,
                        )
                    }

                    currentWeatherResponse.snow != null -> {
                        stringResource(
                            R.string.label_current_snow_expected,
                            currentWeatherResponse.snow.amount,
                        )
                    }

                    else -> stringResource(R.string.label_current_no_precipitations)
                }
            Text(
                text = precipitation,
                modifier =
                    Modifier
                        .align(alignment = Alignment.CenterHorizontally),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_16dp)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_16dp)),
                verticalAlignment = Alignment.Bottom,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.label_current_wind),
                        modifier = Modifier,
                    )
                    Text(
                        text =
                            stringResource(
                                R.string.label_current_wind_speed,
                                currentWeatherResponse.wind.speed,
                            ),
                        modifier = Modifier,
                    )

                    Text(
                        text = stringResource(windDirection(currentWeatherResponse.wind.direction)),
                        modifier = Modifier,
                    )
                    currentWeatherResponse.wind.gust?.let {
                        Text(
                            text = stringResource(
                                R.string.label_current_wind_gust,
                                it
                            ),
                            modifier = Modifier,
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    val cal = Calendar.getInstance(Locale.ENGLISH)
                    cal.timeInMillis = currentWeatherResponse.sunTime.sunRiseTimestamp * 1000L
                    var sunTime = DateFormat.format("hh:mm a", cal).toString()
                    Text(
                        text = stringResource(R.string.label_today_sunrise, sunTime),
                        modifier = Modifier,
                    )
                    cal.timeInMillis = currentWeatherResponse.sunTime.sunSetTimestamp * 1000L
                    sunTime = DateFormat.format("hh:mm a", cal).toString()
                    Text(
                        text =
                            stringResource(
                                R.string.label_today_sunset,
                                sunTime,
                            ),
                        modifier = Modifier,
                    )
                }
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_16dp))
                    .align(Alignment.BottomCenter),
        ) {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = currentWeatherResponse.dataCalculation * 1000L
            val searchTime = DateFormat.format("MM/dd/yyyy hh:mm a", cal).toString()
            Text(
                text = stringResource(R.string.label_data_search_date, searchTime),
            )
        }
    }
}

/**
 * get the local asset from the local asset
 */
private fun convertURLToDrawableId(url: String): Int =
    when (url) {
        "01d" -> R.drawable.ic_1d
        "01n" -> R.drawable.ic_1n
        "02d" -> R.drawable.ic_2d
        "02n" -> R.drawable.ic_2n
        "03d" -> R.drawable.ic_3d
        "03n" -> R.drawable.ic_3n
        "04d" -> R.drawable.ic_4d
        "04n" -> R.drawable.ic_4n
        "09d" -> R.drawable.ic_9d
        "09n" -> R.drawable.ic_9n
        "10d" -> R.drawable.ic_10d
        "10n" -> R.drawable.ic_10n
        "11d" -> R.drawable.ic_11d
        "11n" -> R.drawable.ic_11n
        "13d" -> R.drawable.ic_13d
        "13n" -> R.drawable.ic_13n
        else -> R.drawable.ic_50n
    }

private fun windDirection(direction: Int) =
    when (direction) {
        in 23..66 -> R.string.label_current_wind_north_east
        in 67..111 -> R.string.label_current_wind_east
        in 112..156 -> R.string.label_current_wind_south_east
        in 157..202 -> R.string.label_current_wind_south
        in 203..247 -> R.string.label_current_wind_south_west
        in 248..291 -> R.string.label_current_wind_west
        in 292..337 -> R.string.label_current_wind_north_west
        else -> R.string.label_current_wind_north
    }

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DetailScreenLoadingPreview() {
    DetailScreenLoading(modifier = Modifier.fillMaxSize())
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DetailScreenErrorPreview() {
    DetailScreenError(
        errorMessage = stringResource(R.string.error_server_error),
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DetailWeatherScreenPreview() {
    DetailWeatherScreen(
        currentWeatherResponse =
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
        modifier = Modifier.fillMaxSize(),
    )
}
