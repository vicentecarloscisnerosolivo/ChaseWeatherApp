package com.vcco.weather.ui.screen

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vcco.weather.R
import com.vcco.weather.repository.responses.OpenWeatherServiceResponses
import com.vcco.weather.ui.screens.LastSearchDetailScreen
import org.junit.Rule
import org.junit.Test

class LastSearchDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lastSearchDetail() {
        val currentWeather =
            OpenWeatherServiceResponses.getCurrentWeatherResponseCorrect()
        var currentTemperature = ""
        var note = ""
        composeTestRule.setContent {
            LastSearchDetailScreen(
                lastWeatherResponse = currentWeather,
                onBackClicked = {},
            )
            currentTemperature =
                stringResource(
                    R.string.label_current_temperature_format,
                    currentWeather.temperature.temperature.toInt(),
                )
            note = stringResource(R.string.label_data_last_search_info)
        }

        composeTestRule
            .onNodeWithText(currentTemperature)
            .assertExists()

        composeTestRule
            .onNodeWithText(note)
            .assertExists()
    }
}
