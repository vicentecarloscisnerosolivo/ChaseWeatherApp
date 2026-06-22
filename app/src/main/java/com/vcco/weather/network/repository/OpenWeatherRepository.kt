package com.vcco.weather.network.repository

import com.vcco.weather.network.apiHelper.OpenWeatherApiHelper
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
    ) {
        /**
         * getCurrentWeather from the requested location, could be GPS location or searched location
         * by user
         *
         * @param location String -> Could be following conditions: City or City and Country or City,
         *                 State and Country
         * @param units    String -> Could be Metric or Imperil by user selection
         */

        fun getCurrentWeatherFromLocation(
            location: String,
            unit: String,
        ) = helper.getWeatherCurrentLocation(query = location, units = unit)

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
        fun getInfoFromZipCode(zipCode: String) = helper.getInfoFromZipCode(zip = zipCode)

        /**
         * get location Info from the requested Coordinates
         *
         * @param latitude  Float -> latitude from the location
         * @param longitude Float -> longitude from the location
         *
         *Warning only can be called from not async threat
         */
        fun getLocationInfoFromCoordinates(
            latitude: Float,
            longitude: Float,
        ) = helper.getReverseLocation(
            latitude = latitude,
            longitude = longitude,
        )
    }
