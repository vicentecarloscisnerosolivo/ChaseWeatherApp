package com.vcco.weather.network.utils

internal object NetworkConstants {
    //URL
    const val WEATHER_URL = "data/2.5/weather"
    const val GEOCODE_URL = "geo/1.0/direct"
    const val ZIP_URL = "geo/1.0/zip"
    const val REVERSE_GEOCODE_URL = "geo/1.0/reverse"

    //Commons Request params
    const val QUERY = "q"
    const val APY_KEY = "appid"

    //Weather Request params
    const val UNITS = "units"

    //Zip Request
    const val ZIP = "zip"

    //Reverse Geocode Params
    const val LATITUDE = "lat"
    const val LONGITUDE = "lon"

    const val NOT_FOUND = "not found"
}