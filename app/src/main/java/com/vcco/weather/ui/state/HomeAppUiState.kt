package com.vcco.weather.ui.state

import com.vcco.weather.data.utils.PreferencesConstants
import com.vcco.weather.model.weather.CurrentWeatherResponse


data class HomeUiState(
    val isFromInit: Boolean = false,
    val units: String = PreferencesConstants.DEFAULT_UNIT,
    val homeAppUiState: HomeAppUiState
)

sealed interface HomeAppUiState {
    /**
     * Used to show data for LastSearch Search
     */
    data class LastWeather(
        val lastWeatherResponse: CurrentWeatherResponse
    ) : HomeAppUiState

    /**
     * Used to show network error
     */
    data class NetworkError(
        val error: String
    ) : HomeAppUiState

    /**
     * Used when there is no Data to show
     */
    object FirstRun : HomeAppUiState
}
