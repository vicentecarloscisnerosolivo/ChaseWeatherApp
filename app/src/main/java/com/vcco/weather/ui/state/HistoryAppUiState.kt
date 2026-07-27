package com.vcco.weather.ui.state

import com.vcco.weather.model.weather.CurrentWeatherResponse

sealed interface HistoryAppUiState {
    data class CompleteHistory(
        val weatherSearchList: List<CurrentWeatherResponse>,
    ) : HistoryAppUiState

    data class LastFive(
        val weatherSearchList: List<CurrentWeatherResponse>,
    ) : HistoryAppUiState

    object Loading : HistoryAppUiState

    data class Error(
        val error: String,
    ) : HistoryAppUiState
}
