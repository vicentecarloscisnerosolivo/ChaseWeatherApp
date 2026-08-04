package com.vcco.weather.network.repository

import android.content.Context
import android.util.Log
import com.vcco.weather.R
import com.vcco.weather.db.dao.WeatherDao
import com.vcco.weather.db.relations.CurrentWeatherWithDetails
import com.vcco.weather.model.errors.WeatherErrorResponse
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelper
import com.vcco.weather.network.utils.NetworkConstants
import com.vcco.weather.network.utils.toConditions
import com.vcco.weather.network.utils.toCoordinates
import com.vcco.weather.network.utils.toCurrentClouds
import com.vcco.weather.network.utils.toCurrentWeather
import com.vcco.weather.network.utils.toCurrentWeatherResponse
import com.vcco.weather.network.utils.toRain
import com.vcco.weather.network.utils.toSnow
import com.vcco.weather.network.utils.toSunTime
import com.vcco.weather.network.utils.toTemperature
import com.vcco.weather.network.utils.toWind
import kotlinx.coroutines.rx3.awaitFirst
import javax.inject.Inject

/**
 * Repository for OpenWeatherService, do network calls
 *
 * @Inject: OpenWeatherApiHelper
 */
class OpenWeatherRepository
    @Inject
    constructor(
        private val helper: OpenWeatherApiHelper,
        private val dao: WeatherDao,
    ) {
        private lateinit var listOfSearchedWeather: MutableList<CurrentWeatherWithDetails>

        fun populateData() {
            listOfSearchedWeather = dao.getLastWeatherSearch()
        }

        suspend fun getCurrentWeatherFromLocation(
            location: String,
            unit: String,
            isFromZipCode: Boolean,
            context: Context,
        ): CurrentWeatherResponse? {
            try {
                val response = helper.getWeatherCurrentLocation(location, unit).awaitFirst()
                if (response.isSuccessful) {
                    response.body()?.let {
                        val deactivateWeather = getDeactivateWeather()
                        val currentWeather = it.toCurrentWeather()
                        val parentId =
                            dao.insertNewWeatherAndRemoveOld(
                                currentWeather = currentWeather,
                                deactivateWeather = deactivateWeather?.currentWeather,
                                clouds = it.toCurrentClouds(),
                                conditions = it.toConditions(),
                                coordinates = it.toCoordinates(),
                                rain = it.toRain(),
                                snow = it.toSnow(),
                                sunTime = it.toSunTime(),
                                temperature = it.toTemperature(),
                                wind = it.toWind(),
                            )

                        switchWeathers(
                            currentWeatherId = parentId,
                            deactivateWeather = deactivateWeather,
                        )
                        return it
                    }
                } else if ((
                        response.raw().message == NetworkConstants.NOT_FOUND ||
                            response.raw().code == NetworkConstants.NOT_FOUND_CODE
                    ) &&
                    !isFromZipCode
                ) {
                    Log.i(TAG, "Retrieving data from zip code")
                    getInfoFromZipCode(location, unit, context)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw WeatherErrorResponse(
                    e.message
                        ?: context.getString(R.string.error_server_error),
                )
            }
            return null
        }

        /**
         * get the location information like coordinates local names from the name of the City
         *
         * @param location String -> Could be following conditions: City or City and Country or City, State and Country
         */
        fun getLocationInfoFromName(location: String) = helper.getLocationInfoFromName(query = location)

        /**
         * get the location info for the giving ZipCode
         *
         * @param zipCode String -> , must use ZipCode,Country Code ie. 90210, US
         */
        suspend fun getInfoFromZipCode(
            zipCode: String,
            unit: String,
            context: Context,
        ): CurrentWeatherResponse? {
            try {
                val zipCodeResponse = helper.getInfoFromZipCode(zipCode).awaitFirst()
                if (zipCodeResponse.isSuccessful) {
                    zipCodeResponse.body()?.let {
                        val query =
                            context.getString(
                                R.string.format_string_for_city_query,
                                it.name,
                                "",
                                it.country,
                            )
                        return getCurrentWeatherFromLocation(
                            location = query,
                            unit = unit,
                            isFromZipCode = true,
                            context = context,
                        )
                    }
                } else {
                    throw WeatherErrorResponse(
                        context.getString(R.string.error_not_found_location),
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw WeatherErrorResponse(
                    e.message ?: context.getString(R.string.error_server_error),
                )
            }
            return null
        }

        /**
         * get location Info from the requested Coordinates
         *
         * @param latitude  Float -> latitude from the location
         * @param longitude Float -> longitude from the location
         *
         *Warning only can be called from not async threat
         */
        suspend fun getLocationInfoFromCoordinates(
            latitude: Float,
            longitude: Float,
            unit: String,
            context: Context,
        ): CurrentWeatherResponse? {
            val locationResponse =
                helper
                    .getReverseLocation(
                        latitude = latitude,
                        longitude = longitude,
                    ).awaitFirst()
            if (locationResponse.isSuccessful) {
                locationResponse.body()?.firstOrNull()?.let {
                    val location =
                        context.getString(
                            R.string.format_string_for_city_query,
                            it.name,
                            it.state,
                            it.country,
                        )
                    return getCurrentWeatherFromLocation(
                        location = location,
                        unit = unit,
                        isFromZipCode = true,
                        context = context,
                    )
                }
            } else {
                throw WeatherErrorResponse(
                    context.getString(R.string.error_server_error),
                )
            }
            return null
        }

        /**
         * get mostRecentWeather searched from Db
         */

        fun getMostRecentWeatherSearch(): CurrentWeatherResponse? {
            val lastWeather = dao.getLastWeatherSearched()

            return lastWeather?.toCurrentWeatherResponse()
        }

        fun getDeactivateWeather() = if (listOfSearchedWeather.size == 5) listOfSearchedWeather[4] else null

        fun switchWeathers(
            currentWeatherId: Long,
            deactivateWeather: CurrentWeatherWithDetails?,
        ) {
            val currentWeather = dao.getCurrentWeatherWithDetails(currentWeatherId)
            listOfSearchedWeather.add(element = currentWeather, index = 0)
            deactivateWeather?.let {
                dao.deactivateWeather(deactivateWeather.currentWeather)
                listOfSearchedWeather.remove(deactivateWeather)
            }
//            Log.i(TAG, "Updating list size")
//            for (index in 5..<listOfSearchedWeather.size){
//                dao.deactivateWeather(listOfSearchedWeather[index].currentWeather.copy(isActive = false))
//                Log.i(TAG, "value updated")
//            }
        }

        fun getListOfRecentWeatherSearch() =
            if (listOfSearchedWeather.size == 5) {
                listOfSearchedWeather.toList().map { currentWeather ->
                    currentWeather.toCurrentWeatherResponse()
                }
            } else {
                Log.i(TAG, "The recente search size is ${listOfSearchedWeather.size}")
                null
            }

        fun getAllSearchedWeather() =
            dao.getAllWeathersWithRelations().map { currentWeather ->
                currentWeather.toCurrentWeatherResponse()
            }

        companion object {
            private const val TAG = "OpenWeatherRepository"
        }
    }
