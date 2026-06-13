package com.vcco.weather.model.weather

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Country code and expected sun rise and sun set in the requested location
 */
data class SunTime(
    val country: String,
    @SerializedName(Constants.SUN_TIME_SUNRISE_TIME_RESPONSE)
    val sunRiseTimestamp: Long,
    @SerializedName(Constants.SUN_TIME_SUNSET_TIME_RESPONSE)
    val sunSetTimestamp: Long
)
