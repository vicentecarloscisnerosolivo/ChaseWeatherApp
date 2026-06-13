package com.vcco.weather.model.weather

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Rain expected in the next hour in the requested location, can be a null value
 *
 * NOTE: Value given in mm/h from API
 */
data class Rain(
    @SerializedName(Constants.RAIN_AMOUNT_RESPONSE)
    val amount: Float
)
