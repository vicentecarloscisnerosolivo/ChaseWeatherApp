package com.vcco.weather.model.weather

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Represent the Coordinates of the requested location
 */
data class Coordinates(
    @SerializedName(Constants.LONGITUDE_RESPONSE)
    val longitude: Float,
    @SerializedName(Constants.LATITUDE_RESPONSE)
    val latitude: Float
)
