package com.vcco.weather.model.weather

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Represent current Conditions of the requested Location
 * ie: Cloudy, Rainy, Clear
 */
data class Conditions(
    val id: Int,
    @SerializedName(Constants.CONDITIONS_MAIN_CONDITION_RESPONSE)
    val condition: String,
    val description: String,
    val icon: String,
)
