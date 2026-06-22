package com.vcco.weather.ui.state

import com.vcco.weather.model.weather.CurrentWeatherResponse

sealed interface DetailAppUiState {
    /**
     * Used to show data for Current Search
     */
    data class Success(
        val currentWeatherResponse: CurrentWeatherResponse,
    ) : DetailAppUiState

    /**
     * Used to show Error on Last Search
     */
    data class Error(
        val error: String,
    ) : DetailAppUiState

    /**
     * Used to show Loading Screen
     */
    object Loading : DetailAppUiState
}
