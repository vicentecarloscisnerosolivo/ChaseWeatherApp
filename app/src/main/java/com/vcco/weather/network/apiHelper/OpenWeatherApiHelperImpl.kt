package com.vcco.weather.network.apiHelper

import com.vcco.weather.BuildConfig
import com.vcco.weather.network.service.OpenWeatherService
import javax.inject.Inject

class OpenWeatherApiHelperImp @Inject constructor(
    private val service: OpenWeatherService
) : OpenWeatherApiHelper {


    override fun getWeatherCurrentLocation(
        query: String,
        units: String
    ) = service.getCurrentWeather(
        query = query,
        apiKey = API_KEY,
        units = units
    )

    override fun getLocationInfoFromName(query: String) = service.getInfoWithLocationName(
        query = query,
        apiKey = API_KEY
    )

    override fun getInfoFromZipCode(zip: String) = service.getInfoFromZipCode(
        zip = zip,
        apiKey = API_KEY
    )

    override fun getReverseLocation(
        latitude: Float,
        longitude: Float
    ) = service.getReverseLocation(
        latitude = latitude,
        longitude = longitude,
        apiKey = API_KEY
    )

    companion object {
        private const val API_KEY = BuildConfig.API_KEY
    }
}
