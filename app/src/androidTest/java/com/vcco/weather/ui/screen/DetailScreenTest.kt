package com.vcco.weather.ui.screen

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.vcco.weather.R
import com.vcco.weather.repository.responses.OpenWeatherServiceResponses
import com.vcco.weather.ui.screens.DetailScreen
import com.vcco.weather.ui.state.DetailAppUiState
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailScreenLoading() {
        composeTestRule.setContent {
            DetailScreen(
                detailAppUiState = DetailAppUiState.Loading,
            )
        }

        composeTestRule
            .onNodeWithTag("Loading Info")
            .assertExists()

        composeTestRule
            .onNodeWithText("Current Temperature")
            .assertDoesNotExist()
    }

    @Test
    fun detailScreenError() {
        composeTestRule.setContent {
            DetailScreen(
                detailAppUiState = DetailAppUiState.Error("No Internet"),
            )
        }

        composeTestRule
            .onNodeWithText("No Internet")
            .assertExists()

        composeTestRule
            .onNodeWithText("Current Temperature")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("Loading Info")
            .assertDoesNotExist()
    }

    @Test
    fun detailWeatherScreen() {
        val currentWeather =
            OpenWeatherServiceResponses.getCurrentWeatherResponseCorrect()
        var currentTemperature = ""
        composeTestRule.setContent {
            DetailScreen(
                detailAppUiState = DetailAppUiState.Success(currentWeather),
            )
            currentTemperature =
                stringResource(
                    R.string.label_current_temperature_format,
                    currentWeather.temperature.temperature.toInt(),
                )
        }

        composeTestRule
            .onNodeWithText("Current Temperature")
            .assertExists()

        composeTestRule
            .onNodeWithText(currentTemperature)
            .assertExists()

        composeTestRule
            .onNodeWithTag("Loading Info")
            .assertDoesNotExist()
    }
}
