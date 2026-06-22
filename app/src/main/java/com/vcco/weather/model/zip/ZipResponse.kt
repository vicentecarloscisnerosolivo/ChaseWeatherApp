package com.vcco.weather.model.zip

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Geocode info from specific Zip Code retrieved from https://openweathermap.org/
 */
data class ZipResponse(
    @SerializedName(Constants.ZIP_ZIP_RESPONSE)
    val zipCode: String,
    @SerializedName(Constants.NAME_RESPONSE)
    val name: String,
    @SerializedName(Constants.LATITUDE_RESPONSE)
    val latitude: Float,
    @SerializedName(Constants.LONGITUDE_RESPONSE)
    val longitude: Float,
    @SerializedName(Constants.COUNTRY_RESPONSE)
    val country: String,
)
