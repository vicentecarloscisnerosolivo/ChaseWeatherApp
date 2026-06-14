package com.vcco.weather.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vcco.weather.R
import com.vcco.weather.data.Preference
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.network.repository.OpenWeatherRepository
import com.vcco.weather.network.repository.OpenWeatherRepositoryJava
import com.vcco.weather.network.utils.NetworkConstants
import com.vcco.weather.ui.state.DetailAppUiState
import com.vcco.weather.ui.state.HomeAppUiState
import com.vcco.weather.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sp: Preference,
    private val repository: OpenWeatherRepository,
    private val repositoryJava: OpenWeatherRepositoryJava,
) : ViewModel() {
    /**
     * Value used for Log identification
     */
    private val TAG = javaClass.name

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
                homeAppUiState = HomeAppUiState.FirstRun
            )
        )
    val homeScreenUiState = _homeScreenUiState.asStateFlow()

    /**
     * init data and get units saved data preference and last search
     */
    init {
        val lastSearch = sp.lastSearch
        if (lastSearch != "") {
            val lastSearchResponse = Gson().fromJson(lastSearch, CurrentWeatherResponse::class.java)
            updateHomeUIState(
                isFromInit = true,
                units = sp.unitValue,
                newHomeAppUiState = HomeAppUiState.LastWeather(
                    lastWeatherResponse = lastSearchResponse
                )
            )
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
    fun searchCityWeather(location: String, isFromZipCode: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            updateDetailUIState(DetailAppUiState.Loading)
            repository.getCurrentWeatherFromLocation(location, sp.unitValue)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            updateDetailUIState(
                                DetailAppUiState.Success(it)
                            )
                            saveLastSearch(it)
                        }
                    } else if ((response.raw().message == NetworkConstants.NOT_FOUND || response.raw().code == 404) && !isFromZipCode) {
                        Log.i(TAG, "Retrieving data from zip code")
                        getInfoFromZipCode(location)
                    }
                }, {
                    it.printStackTrace()
                    updateDetailUIState(
                        DetailAppUiState.Error(
                            it.message ?: context.getString(R.string.error_server_error)
                        )
                    )
                })
        }
    }

    /**
     * Search with [zipCode] to get name of the city, state and country
     */

    fun getInfoFromZipCode(zipCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateDetailUIState(DetailAppUiState.Loading)
            repository.getInfoFromZipCode(zipCode)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            val query = context.getString(
                                R.string.format_string_for_city_query,
                                data.name,
                                "",
                                data.country
                            )
                            searchCityWeather(query, isFromZipCode = true)
                        } else {
                            updateDetailUIState(
                                DetailAppUiState.Error(context.getString(R.string.error_not_found_location))
                            )
                        }
                    } else if (response.raw().code == 404 || response.raw().message == NetworkConstants.NOT_FOUND) {
                        updateDetailUIState(
                            DetailAppUiState.Error(context.getString(R.string.error_not_found_location))
                        )
                    }
                }, {
                    updateDetailUIState(
                        DetailAppUiState.Error(
                            it.message ?: context.getString(R.string.error_server_error)
                        )
                    )
                })
        }
    }

    /**
     * Search current weatherInfo from [latitude] and [longitude], this only
     * accessible when user request it manually duo time
     */
    fun searchWeatherFromLocation(latitude: Float, longitude: Float) {
        updateDetailUIState(DetailAppUiState.Loading)
        viewModelScope.launch {
            repositoryJava.getLocationInfoFromCoordinates(latitude, longitude)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        val resultCity = response.body()?.firstOrNull()
                        if (resultCity != null) {
                            /*
                             * when we have a success search of city name, create a query
                             * to get current weather from location
                             */
                            val location = context.getString(
                                R.string.format_string_for_city_query,
                                resultCity.name,
                                resultCity.state,
                                resultCity.country
                            )
                            searchCityWeather(location)
                        } else {
                            updateDetailUIState(DetailAppUiState.Error(context.getString(R.string.error_server_error)))
                        }
                    }
                }, {
                    it.printStackTrace()
                    updateDetailUIState(DetailAppUiState.Error(context.getString(R.string.error_server_error)))
                })
        }
    }

    /**
     * Save the current Weather Search and show when launch the app or when user request it
     */
    private fun saveLastSearch(lastWeatherSearch: CurrentWeatherResponse) {
        val json = Gson().toJson(lastWeatherSearch)
        sp.saveSearch(json)
        updateHomeUIState(
            newHomeAppUiState = HomeAppUiState.LastWeather(
                lastWeatherResponse = lastWeatherSearch,
            )
        )
    }

    /**
     * Update unit in Home UI State
     */
    private fun updateUnitsInHomeState(units: String) {
        updateHomeUIState(
            units = units
        )
    }

    fun updateIsFromInitHomeUIState() {
        updateHomeUIState(
            isFromInit = false
        )
    }

    /**
     * Update Home UI State and updating the Compose UI
     */
    private fun updateHomeUIState(
        isFromInit: Boolean = _homeScreenUiState.value.isFromInit,
        units: String = _homeScreenUiState.value.units,
        newHomeAppUiState: HomeAppUiState = _homeScreenUiState.value.homeAppUiState
    ) {
        _homeScreenUiState.update { currentState ->
            currentState.copy(
                isFromInit = isFromInit,
                units = units,
                homeAppUiState = newHomeAppUiState
            )
        }
    }


    /**
     * Update Detail UI State and updating the Compose UI
     */
    private fun updateDetailUIState(
        newDetailAppUiState: DetailAppUiState
    ) {
        _detailScreenUiState.update { newDetailAppUiState }
    }

}
