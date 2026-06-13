package com.vcco.weather.model.weather

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Snow expected in the current requested location, can be a null value
 *
 *  NOTE: Value given in mm/h from API
 */
data class Snow(
    @SerializedName(Constants.SNOW_AMOUNT_RESPONSE)
    val amount: Float
)