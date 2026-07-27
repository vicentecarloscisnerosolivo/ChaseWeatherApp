package com.vcco.weather.network.repository

import android.content.Context
import android.util.Log
import com.vcco.weather.R
import com.vcco.weather.db.dao.WeatherDao
import com.vcco.weather.db.model.CurrentWeather
import com.vcco.weather.model.errors.WeatherErrorResponse
import com.vcco.weather.model.weather.Clouds
import com.vcco.weather.model.weather.Conditions
import com.vcco.weather.model.weather.Coordinates
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.model.weather.Rain
import com.vcco.weather.model.weather.Snow
import com.vcco.weather.model.weather.SunTime
import com.vcco.weather.model.weather.Temperature
import com.vcco.weather.model.weather.Wind
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelper
import com.vcco.weather.network.utils.NetworkConstants
import com.vcco.weather.network.utils.toConditions
import com.vcco.weather.network.utils.toCoordinates
import com.vcco.weather.network.utils.toCurrentClouds
import com.vcco.weather.network.utils.toCurrentWeather
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
        private lateinit var listOfSearchedWeather: MutableList<CurrentWeather>

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
                                deactivateWeather = deactivateWeather,
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
                            currentWeather = currentWeather,
                            deactivateWeather = deactivateWeather,
                            parentId = parentId,
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
            lastWeather?.let { currentWeather ->
                val clouds = dao.getCloudsForWeather(currentWeather.uId)
                val coordinates = dao.getCoordinatesForWeather(currentWeather.uId)
                val conditions = dao.getConditionForWeather(currentWeather.uId)
                val rain = dao.getRainForWeather(currentWeather.uId)
                val snow = dao.getSnowForWeather(currentWeather.uId)
                val sunTime = dao.getSunTimeForWeather(currentWeather.uId)
                val temperature = dao.getTemperatureForWeather(currentWeather.uId)
                val wind = dao.getWindForWeather(currentWeather.uId)

                return CurrentWeatherResponse(
                    coordinates =
                        Coordinates(
                            longitude = coordinates.longitude,
                            latitude = coordinates.latitude,
                        ),
                    conditions =
                        conditions.map {
                            Conditions(
                                id = it.id,
                                condition = it.condition,
                                description = it.description,
                                icon = it.icon,
                            )
                        },
                    temperature =
                        Temperature(
                            temperature = temperature.temperature,
                            feelsLike = temperature.feelsLike,
                            minTemperature = temperature.minTemperature,
                            maxTemperature = temperature.maxTemperature,
                            humidity = temperature.humidity,
                        ),
                    visibility = currentWeather.visibility,
                    wind =
                        Wind(
                            speed = wind.speed,
                            direction = wind.direction,
                        ),
                    clouds =
                        Clouds(
                            coverage = clouds.coverage,
                        ),
                    rain = if (rain == null) null else Rain(amount = rain.amount),
                    snow = if (snow == null) null else Snow(amount = snow.amount),
                    dataCalculation = currentWeather.dataCalculation,
                    sunTime =
                        SunTime(
                            country = sunTime.country,
                            sunRiseTimestamp = sunTime.sunRiseTimestamp,
                            sunSetTimestamp = sunTime.sunSetTimestamp,
                        ),
                    timeZone = currentWeather.timeZone,
                    id = currentWeather.id,
                    name = currentWeather.name,
                )
            }
            return null
        }

        fun getDeactivateWeather() = if (listOfSearchedWeather.size == 5) listOfSearchedWeather[4] else null

        fun switchWeathers(
            currentWeather: CurrentWeather,
            deactivateWeather: CurrentWeather?,
            parentId: Long,
        ) {
            listOfSearchedWeather.add(element = currentWeather.copy(uId = parentId), index = 0)
            deactivateWeather?.let {
                dao.deactivateWeather(deactivateWeather)
                listOfSearchedWeather.remove(deactivateWeather)
            }
        }

        fun getListOfRecentWeatherSearch() =
            if (listOfSearchedWeather.size >= 5) {
                listOfSearchedWeather.toList().map { currentWeather ->
                    CurrentWeatherResponse(
                        coordinates =
                            dao
                                .getCoordinatesForWeather(currentWeather.uId)
                                .let { Coordinates(longitude = it.longitude, latitude = it.latitude) },
                        conditions =
                            dao.getConditionForWeather(currentWeather.uId).let { conditions ->
                                conditions.map {
                                    Conditions(
                                        id = it.id,
                                        condition = it.condition,
                                        description = it.description,
                                        icon = it.icon,
                                    )
                                }
                            },
                        temperature =
                            dao.getTemperatureForWeather(currentWeather.uId).let {
                                Temperature(
                                    temperature = it.temperature,
                                    feelsLike = it.feelsLike,
                                    minTemperature = it.minTemperature,
                                    maxTemperature = it.maxTemperature,
                                    humidity = it.humidity,
                                )
                            },
                        visibility = currentWeather.visibility,
                        wind =
                            dao.getWindForWeather(currentWeather.uId).let {
                                Wind(
                                    speed = it.speed,
                                    direction = it.direction,
                                )
                            },
                        clouds =
                            dao.getCloudsForWeather(currentWeather.uId).let {
                                Clouds(coverage = it.coverage)
                            },
                        rain =
                            dao.getRainForWeather(currentWeather.uId)?.let {
                                Rain(amount = it.amount)
                            },
                        snow =
                            dao.getSnowForWeather(currentWeather.uId)?.let {
                                Snow(amount = it.amount)
                            },
                        dataCalculation = currentWeather.dataCalculation,
                        sunTime =
                            dao.getSunTimeForWeather(currentWeather.uId).let {
                                SunTime(
                                    country = it.country,
                                    sunRiseTimestamp = it.sunRiseTimestamp,
                                    sunSetTimestamp = it.sunSetTimestamp,
                                )
                            },
                        timeZone = currentWeather.timeZone,
                        name = currentWeather.name,
                        id = currentWeather.id,
                    )
                }
            } else {
                null
            }

        fun getAllSearchedWeather() =
            dao.getAllWeatherSearched().toList().map { currentWeather ->
                CurrentWeatherResponse(
                    coordinates =
                        dao
                            .getCoordinatesForWeather(currentWeather.uId)
                            .let { Coordinates(longitude = it.longitude, latitude = it.latitude) },
                    conditions =
                        dao.getConditionForWeather(currentWeather.uId).let { conditions ->
                            conditions.map {
                                Conditions(
                                    id = it.id,
                                    condition = it.condition,
                                    description = it.description,
                                    icon = it.icon,
                                )
                            }
                        },
                    temperature =
                        dao.getTemperatureForWeather(currentWeather.uId).let {
                            Temperature(
                                temperature = it.temperature,
                                feelsLike = it.feelsLike,
                                minTemperature = it.minTemperature,
                                maxTemperature = it.maxTemperature,
                                humidity = it.humidity,
                            )
                        },
                    visibility = currentWeather.visibility,
                    wind =
                        dao.getWindForWeather(currentWeather.uId).let {
                            Wind(
                                speed = it.speed,
                                direction = it.direction,
                            )
                        },
                    clouds =
                        dao.getCloudsForWeather(currentWeather.uId).let {
                            Clouds(coverage = it.coverage)
                        },
                    rain =
                        dao.getRainForWeather(currentWeather.uId)?.let {
                            Rain(amount = it.amount)
                        },
                    snow =
                        dao.getSnowForWeather(currentWeather.uId)?.let {
                            Snow(amount = it.amount)
                        },
                    dataCalculation = currentWeather.dataCalculation,
                    sunTime =
                        dao.getSunTimeForWeather(currentWeather.uId).let {
                            SunTime(
                                country = it.country,
                                sunRiseTimestamp = it.sunRiseTimestamp,
                                sunSetTimestamp = it.sunSetTimestamp,
                            )
                        },
                    timeZone = currentWeather.timeZone,
                    name = currentWeather.name,
                    id = currentWeather.id,
                )
            }

        companion object {
            private const val TAG = "OpenWeatherRepository"
        }
    }
