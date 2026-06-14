package com.vcco.weather.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vcco.weather.repository.responses.OpenWeatherServiceResponses
import com.vcco.weather.ui.screens.HomeScreen
import com.vcco.weather.ui.state.HomeAppUiState
import com.vcco.weather.ui.state.HomeUiState
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun HomeScreenTestFirstLaunch() {
        composeTestRule.setContent {
            HomeScreen(
                homeUiState = HomeUiState(
                    homeAppUiState = HomeAppUiState.FirstRun
                ),
                onSearchWeatherCLicked = {},
                onSearchWeatherCurrentLocationClicked = {},
                onLastWeatherSearchClicked = {},
                onUnitSelectionChanged = {}
            )
        }

        composeTestRule
            .onNodeWithText("Type City or Zip Code")
            .assertExists()


        composeTestRule
            .onNodeWithText("Show last search")
            .assertDoesNotExist()
    }

    @Test
    fun HomeScreenTestError() {
        composeTestRule.setContent {
            HomeScreen(
                homeUiState = HomeUiState(
                    homeAppUiState = HomeAppUiState.NetworkError("No Internet")
                ),
                onSearchWeatherCLicked = {},
                onSearchWeatherCurrentLocationClicked = {},
                onLastWeatherSearchClicked = {},
                onUnitSelectionChanged = {}
            )
        }

        composeTestRule
            .onNodeWithText("Type City or Zip Code")
            .assertDoesNotExist()


        composeTestRule
            .onNodeWithText("No Internet")
            .assertExists()
    }

    @Test
    fun HomeScreenTestLastSearch() {
        composeTestRule.setContent {
            HomeScreen(
                homeUiState = HomeUiState(
                    homeAppUiState = HomeAppUiState.LastWeather(
                        OpenWeatherServiceResponses.Companion.getCurrentWeatherResponseCorrect()
                    )
                ),
                onSearchWeatherCLicked = {},
                onSearchWeatherCurrentLocationClicked = {},
                onLastWeatherSearchClicked = {},
                onUnitSelectionChanged = {}
            )
        }

        composeTestRule
            .onNodeWithText("Type City or Zip Code")
            .assertExists()


        composeTestRule
            .onNodeWithText("Show last search")
            .assertExists()
    }
}