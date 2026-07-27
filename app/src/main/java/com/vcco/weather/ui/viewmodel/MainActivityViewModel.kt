package com.vcco.weather.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vcco.weather.R
import com.vcco.weather.data.Preference
import com.vcco.weather.model.errors.WeatherErrorResponse
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.network.repository.OpenWeatherRepository
import com.vcco.weather.network.repository.OpenWeatherRepositoryJava
import com.vcco.weather.ui.state.DetailAppUiState
import com.vcco.weather.ui.state.HistoryAppUiState
import com.vcco.weather.ui.state.HomeAppUiState
import com.vcco.weather.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        private val sp: Preference,
        private val repository: OpenWeatherRepository,
        private val repositoryJava: OpenWeatherRepositoryJava,
    ) : ViewModel() {
        /**
         * Value for Detail UI Weather state, if there is last search saved, will be shown
         */
        private val _detailScreenUiState =
            MutableStateFlow<DetailAppUiState>(DetailAppUiState.Loading)
        val detailScreenUiState: StateFlow<DetailAppUiState> = _detailScreenUiState.asStateFlow()

        /**
         * Value used for Home Ui State
         */
        private val _homeScreenUiState =
            MutableStateFlow(
                HomeUiState(
                    isFromInit = false,
                    units = sp.unitValue,
                    homeAppUiState = HomeAppUiState.FirstRun,
                ),
            )
        val homeScreenUiState = _homeScreenUiState.asStateFlow()

        private val _historyScreenUiState =
            MutableStateFlow<HistoryAppUiState>(HistoryAppUiState.Loading)
        val historyScreenUiState = _historyScreenUiState.asStateFlow()

        /**
         * init data and get units saved data preference and last search
         */
        init {
            viewModelScope.launch(Dispatchers.IO) {
                repository.populateData()
                val lastSearch = repository.getMostRecentWeatherSearch()
                val mostRecentSearch = repository.getListOfRecentWeatherSearch()

                lastSearch?.let { weatherResponse ->
                    updateHomeUIState(
                        isFromInit = true,
                        units = sp.unitValue,
                        canShowLastFiveSearch =
                            if (mostRecentSearch != null) mostRecentSearch.size >= 5 else false,
                        newHomeAppUiState =
                            HomeAppUiState.LastWeather(
                                lastWeatherResponse = weatherResponse,
                            ),
                    )
                }
            }
        }

        /**
         * Update the unit preference for Temperature values
         * @param units: String
         */
        fun updateUnitPreference(units: String) {
            updateUnitsInHomeState(units)
            sp.saveUnitValue(units)
        }

        /**
         * Search weather from [location], feel free to add city name, state if you place and Country
         *
         */
        fun searchCityWeather(
            location: String,
            isFromZipCode: Boolean = false,
            context: Context,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                updateDetailUIState(DetailAppUiState.Loading)
                try {
                    val result =
                        repository.getCurrentWeatherFromLocation(
                            location,
                            sp.unitValue,
                            isFromZipCode,
                            context,
                        )
                    if (result != null) {
                        updateDetailUIState(
                            DetailAppUiState.Success(
                                result,
                            ),
                        )
                        saveLastSearch(result)
                    } else {
                        updateDetailUIState(
                            DetailAppUiState.Error(
                                context.getString(R.string.error_no_internet_connection),
                            ),
                        )
                    }
                } catch (e: WeatherErrorResponse) {
                    e.printStackTrace()
                    updateDetailUIState(
                        DetailAppUiState.Error(
                            e.message ?: context.getString(R.string.error_not_found_location),
                        ),
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateDetailUIState(
                        DetailAppUiState.Error(
                            e.message ?: context.getString(R.string.error_not_found_location),
                        ),
                    )
                }
            }
        }

        /**
         * Search with [zipCode] to get name of the city, state and country
         */

        fun getInfoFromZipCode(
            zipCode: String,
            context: Context,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                updateDetailUIState(DetailAppUiState.Loading)
                try {
                    val result =
                        repository.getInfoFromZipCode(
                            zipCode = zipCode,
                            unit = sp.unitValue,
                            context = context,
                        )
                    if (result != null) {
                        updateDetailUIState(
                            DetailAppUiState.Success(
                                result,
                            ),
                        )
                        saveLastSearch(result)
                    } else {
                        updateDetailUIState(
                            DetailAppUiState.Error(
                                context.getString(R.string.error_no_internet_connection),
                            ),
                        )
                    }
                } catch (e: WeatherErrorResponse) {
                    e.printStackTrace()
                    updateDetailUIState(
                        DetailAppUiState.Error(
                            e.message ?: context.getString(R.string.error_not_found_location),
                        ),
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateDetailUIState(
                        DetailAppUiState.Error(
                            e.message ?: context.getString(R.string.error_server_error),
                        ),
                    )
                }
            }
        }

        /**
         * Search current weatherInfo from [latitude] and [longitude], this only
         * accessible when user request it manually duo time
         */
        fun searchWeatherFromLocation(
            latitude: Float,
            longitude: Float,
            context: Context,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                updateDetailUIState(DetailAppUiState.Loading)
                try {
                    val result =
                        repository.getLocationInfoFromCoordinates(
                            latitude,
                            longitude,
                            sp.unitValue,
                            context,
                        )
                    if (result != null) {
                        updateDetailUIState(
                            DetailAppUiState.Success(result),
                        )
                    } else {
                        Log.i(TAG, "The result is null")
                        updateDetailUIState(
                            DetailAppUiState.Error(
                                context.getString(R.string.error_no_internet_connection),
                            ),
                        )
                    }
                } catch (e: WeatherErrorResponse) {
                    e.printStackTrace()
                    updateDetailUIState(
                        DetailAppUiState.Error(
                            e.message ?: context.getString(R.string.error_not_found_location),
                        ),
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateDetailUIState(
                        DetailAppUiState.Error(
                            e.message ?: context.getString(R.string.error_server_error),
                        ),
                    )
                }
            }
        }

        /**
         * Save the current Weather Search and show when launch the app or when user request it
         */
        private fun saveLastSearch(lastWeatherSearch: CurrentWeatherResponse) {
            updateHomeUIState(
                newHomeAppUiState =
                    HomeAppUiState.LastWeather(
                        lastWeatherResponse = lastWeatherSearch,
                    ),
            )
        }

        /**
         * Update unit in Home UI State
         */
        private fun updateUnitsInHomeState(units: String) {
            updateHomeUIState(
                units = units,
            )
        }

        fun updateIsFromInitHomeUIState() {
            updateHomeUIState(
                isFromInit = false,
            )
        }

        fun getMostFiveRecentWeatherSearch(context: Context) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    updateHistoryUiState(HistoryAppUiState.Loading)
                    val result = repository.getListOfRecentWeatherSearch()
                    if (result != null) {
                        updateHistoryUiState(HistoryAppUiState.LastFive(result))
                    } else {
                        updateHistoryUiState(
                            HistoryAppUiState.Error(
                                context.getString(R.string.error_local_database),
                            ),
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateHistoryUiState(
                        HistoryAppUiState.Error(
                            e.message ?: context.getString(R.string.error_local_database),
                        ),
                    )
                }
            }
        }

        fun getAllWeatherSearch(context: Context) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    updateHistoryUiState(HistoryAppUiState.Loading)
                    val result = repository.getAllSearchedWeather()
                    updateHistoryUiState(
                        HistoryAppUiState.CompleteHistory(result),
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateHistoryUiState(
                        HistoryAppUiState.Error(
                            e.message ?: context.getString(R.string.error_local_database),
                        ),
                    )
                }
            }
        }

        /**
         * Update Home UI State and updating the Compose UI
         */
        private fun updateHomeUIState(
            isFromInit: Boolean = _homeScreenUiState.value.isFromInit,
            units: String = _homeScreenUiState.value.units,
            canShowLastFiveSearch: Boolean = _homeScreenUiState.value.canShowLastFiveSearch,
            newHomeAppUiState: HomeAppUiState = _homeScreenUiState.value.homeAppUiState,
        ) {
            _homeScreenUiState.update { currentState ->
                currentState.copy(
                    isFromInit = isFromInit,
                    units = units,
                    canShowLastFiveSearch = canShowLastFiveSearch,
                    homeAppUiState = newHomeAppUiState,
                )
            }
        }

        /**
         * Update Detail UI State and updating the Compose UI
         */
        private fun updateDetailUIState(newDetailAppUiState: DetailAppUiState) {
            _detailScreenUiState.update { newDetailAppUiState }
        }

        private fun updateHistoryUiState(newHistoryAppUiState: HistoryAppUiState) {
            _historyScreenUiState.update { newHistoryAppUiState }
        }

        companion object {
            /**
             * Value used for Log identification
             */
            private const val TAG = "MainActivityViewModel"
        }
    }
