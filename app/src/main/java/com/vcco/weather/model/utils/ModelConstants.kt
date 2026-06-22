package com.vcco.weather.model.utils

internal object ModelConstants {
    // Commons Response
    const val LONGITUDE_RESPONSE = "lon"
    const val LATITUDE_RESPONSE = "lat"
    const val NAME_RESPONSE = "name"
    const val COUNTRY_RESPONSE = "country"

    // Conditions Response
    const val CONDITIONS_MAIN_CONDITION_RESPONSE = "main"
    const val CONDITIONS_DESCRIPTION_RESPONSE = "description"
    const val CONDITIONS_ICON_RESPONSE = "icon"

    // Temperature Response
    const val CURRENT_TEMPERATURE_RESPONSE = "temp"
    const val CURRENT_TEMPERATURE_FEELS_LIKE_RESPONSE = "feels_like"
    const val CURRENT_HUMIDITY_RESPONSE = "humidity"
    const val CURRENT_MAX_TEMPERATURE_RESPONSE = "temp_max"
    const val CURRENT_MIN_TEMPERATURE_RESPONSE = "temp_min"

    // Wind Response
    const val WIND_SPEED_RESPONSE = "speed"
    const val WIND_DIRECTION_RESPONSE = "deg"

    // Clouds Response
    const val CLOUD_COVERAGE_RESPONSE = "all"

    // Rain Response
    const val RAIN_AMOUNT_RESPONSE = "1h"

    // Snow Response
    const val SNOW_AMOUNT_RESPONSE = "1h"

    // SunTime Response
    const val SUN_TIME_SUNRISE_TIME_RESPONSE = "sunrise"
    const val SUN_TIME_SUNSET_TIME_RESPONSE = "sunset"

    // CurrentWeather Response
    const val CURRENT_WEATHER_COORDINATE_RESPONSE = "coord"
    const val CURRENT_WEATHER_CONDITIONS_RESPONSE = "weather"
    const val CURRENT_WEATHER_TEMPERATURE_RESPONSE = "main"
    const val CURRENT_WEATHER_VISIBILITY_RESPONSE = "visibility"
    const val CURRENT_WEATHER_WIND_RESPONSE = "wind"
    const val CURRENT_WEATHER_CLOUDS_RESPONSE = "clouds"
    const val CURRENT_WEATHER_RAIN_RESPONSE = "rain"
    const val CURRENT_WEATHER_SNOW_RESPONSE = "snow"
    const val CURRENT_WEATHER_DATA_CALCULATION_RESPONSE = "dt"
    const val CURRENT_WEATHER_SUN_TIME_RESPONSE = "sys"
    const val CURRENT_WEATHER_TIME_ZONE_RESPONSE = "timezone"
    const val CURRENT_WEATHER_ID_RESPONSE = "id"
    const val CURRENT_WEATHER_NAME_RESPONSE = "name"

    // Geocode Response
    const val GEOCODE_LOCAL_NAMES_RESPONSE = "local_names"
    const val GEOCODE_STATE_RESPONSE = "state"

    // Zip Response
    const val ZIP_ZIP_RESPONSE = "zip"

    // Error Response
    const val CODE = "code"
}
