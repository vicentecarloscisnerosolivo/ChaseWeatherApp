package com.vcco.weather.model.geoconfig

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

data class GeocodeResponse(
    @SerializedName(Constants.NAME_RESPONSE)
    val name: String? = null,
    @SerializedName(Constants.GEOCODE_LOCAL_NAMES_RESPONSE)
    val localNames: Map<String, String>? = null,
    @SerializedName(Constants.LATITUDE_RESPONSE)
    val latitude: Float? = null,
    @SerializedName(Constants.LONGITUDE_RESPONSE)
    val longitude: Float? = null,
    @SerializedName(Constants.COUNTRY_RESPONSE)
    val country: String? = null,
    @SerializedName(Constants.GEOCODE_STATE_RESPONSE)
    val state: String? = null,
)
