package com.vcco.weather.model.weather

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Current Weather from specific location retrieved from https://openweathermap.org/
 *
 * DEFAULT VALUES: Configured for metric values
 */
data class CurrentWeatherResponse(
    @SerializedName(Constants.CURRENT_WEATHER_COORDINATE_RESPONSE)
    val coordinates: Coordinates,
    @SerializedName(Constants.CURRENT_WEATHER_CONDITIONS_RESPONSE)
    val conditions: List<Conditions>,
    @SerializedName(Constants.CURRENT_WEATHER_TEMPERATURE_RESPONSE)
    val temperature: Temperature,
    @SerializedName(Constants.CURRENT_WEATHER_VISIBILITY_RESPONSE)
    val visibility: Int,
    @SerializedName(Constants.CURRENT_WEATHER_WIND_RESPONSE)
    val wind: Wind,
    @SerializedName(Constants.CURRENT_WEATHER_CLOUDS_RESPONSE)
    val clouds: Clouds,
    @SerializedName(Constants.CURRENT_WEATHER_RAIN_RESPONSE)
    val rain: Rain?,
    @SerializedName(Constants.CURRENT_WEATHER_SNOW_RESPONSE)
    val snow: Snow?,
    /**
     * Time when the data was calculated
     *
     * Value given from the API
     */
    @SerializedName(Constants.CURRENT_WEATHER_DATA_CALCULATION_RESPONSE)
    val dataCalculation: Long,
    @SerializedName(Constants.CURRENT_WEATHER_SUN_TIME_RESPONSE)
    val sunTime: SunTime,
    /**
     * Shift in seconds from UTC
     *
     * Value given from the API
     */
    @SerializedName(Constants.CURRENT_WEATHER_TIME_ZONE_RESPONSE)
    val timeZone: Long,
    /**
     * CityId
     *
     * Value given from the API
     */
    @SerializedName(Constants.CURRENT_WEATHER_ID_RESPONSE)
    val id: Int,
    /**
     * City Name
     *
     * Value given from the API
     */
    @SerializedName(Constants.CURRENT_WEATHER_NAME_RESPONSE)
    val name: String,
)
