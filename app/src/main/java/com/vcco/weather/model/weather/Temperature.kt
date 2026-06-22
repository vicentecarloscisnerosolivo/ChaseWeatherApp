package com.vcco.weather.model.weather

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Represent the current Temperature conditions on the requested location
 * could be given in metric (°C) or imperial values (°F)
 *
 * DEFAULT: Configured for Metric values
 */
data class Temperature(
    /**
     *  Current Temperature, given in metric (°C) or imperial values (°F)
     */
    @SerializedName(Constants.CURRENT_TEMPERATURE_RESPONSE)
    val temperature: Float,
    /**
     * Current real feel temperature, given in metric (°C) or imperial values (°F)
     */
    @SerializedName(Constants.CURRENT_TEMPERATURE_FEELS_LIKE_RESPONSE)
    val feelsLike: Float,
    @SerializedName(Constants.CURRENT_MIN_TEMPERATURE_RESPONSE)
    val minTemperature: Float,
    @SerializedName(Constants.CURRENT_MAX_TEMPERATURE_RESPONSE)
    val maxTemperature: Float,
    val humidity: Int,
)
