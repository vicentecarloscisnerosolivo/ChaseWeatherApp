package com.vcco.weather.ui.toolbar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.vcco.weather.navigation.WeatherScreen
import org.junit.Rule
import org.junit.Test

class WeatherToolbarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun TopAppBastTestHome() {
        composeTestRule.setContent {
            WeatherAppToolBar(
                currentScreen = WeatherScreen.Home,
                canNavigateBack = false,
                navigateUp = {}
            )
        }
        composeTestRule
            .onNodeWithText("Weather")
            .assertExists()
    }

    @Test
    fun TopAppBastTestDetailScreen() {
        composeTestRule.setContent {
            WeatherAppToolBar(
                currentScreen = WeatherScreen.Detail,
                canNavigateBack = true,
                navigateUp = {}
            )
        }
        composeTestRule
            .onNodeWithText("Detail")
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertExists()
    }

    @Test
    fun TopAppBastTestLastSearchScreen() {
        composeTestRule.setContent {
            WeatherAppToolBar(
                currentScreen = WeatherScreen.LastSearch,
                canNavigateBack = true,
                navigateUp = {}
            )
        }
        composeTestRule
            .onNodeWithText("Last Search")
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertExists()
    }
}