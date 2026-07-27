package com.vcco.weather.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vcco.weather.R
import com.vcco.weather.model.weather.CurrentWeatherResponse
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

@Composable
fun DetailHistoryWeatherScreen(
    currentWeatherResponse: CurrentWeatherResponse,
    modifier: Modifier = Modifier,
) {
    val currentConditions = currentWeatherResponse.conditions.first()
    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_16dp)),
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
                        .align(alignment = Alignment.CenterHorizontally),
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
                            .padding(horizontal = dimensionResource(R.dimen.padding_16dp)),
                )
                Row(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
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
                                .padding(horizontal = dimensionResource(R.dimen.padding_8dp))
                                .weight(1f),
                    )
                }
            }
            Text(
                text =
                    stringResource(
                        R.string.label_current_humidity,
                        currentWeatherResponse.temperature.humidity,
                    ),
                modifier =
                    Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(dimensionResource(R.dimen.padding_16dp)),
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
                Column {
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
                    val direction = currentWeatherResponse.wind.direction
                    val windDirection =
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
                    Text(
                        text = stringResource(windDirection),
                        modifier = Modifier,
                    )
                }
                Column {
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
                    .padding(start = dimensionResource(R.dimen.padding_16dp)),
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
