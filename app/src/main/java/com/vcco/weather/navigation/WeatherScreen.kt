package com.vcco.weather.navigation

import androidx.annotation.StringRes
import com.vcco.weather.R

enum class WeatherScreen(
    @StringRes val screenTitle: Int,
) {
    Home(screenTitle = R.string.home_screen),
    Detail(screenTitle = R.string.detail_screen),
    LastSearch(screenTitle = R.string.last_search_screen),
    HistorySearch(screenTitle = R.string.history_search_screen),
}
