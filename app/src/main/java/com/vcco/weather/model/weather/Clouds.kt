package com.vcco.weather.model.weather

import com.google.gson.annotations.SerializedName
import com.vcco.weather.model.utils.ModelConstants as Constants

/**
 * Shows Cloud coverage in the requested location
 */
data class Clouds(
    @SerializedName(Constants.CLOUD_COVERAGE_RESPONSE)
    val coverage: Int
)
