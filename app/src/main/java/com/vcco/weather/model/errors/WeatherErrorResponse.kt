package com.vcco.weather.model.errors

data class WeatherErrorResponse(
    val errorMessage: String,
) : Exception(errorMessage)
