package com.vcco.weather.network.responses

import com.vcco.weather.model.geoconfig.*
import com.vcco.weather.model.weather.*
import com.vcco.weather.model.zip.ZipResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

class OpenWeatherServiceResponses {
    companion object {

        fun getCurrentWeatherResponseCorrect() = CurrentWeatherResponse(
            coordinates = Coordinates(
                longitude = -96.9489f,
                latitude = 32.814f
            ),
            conditions = listOf(
                Conditions(
                    id = 1,
                    condition = "clear",
                    description = "clear",
                    icon = "01d"
                )
            ),
            temperature = Temperature(
                temperature = 90f,
                feelsLike = 92f,
                minTemperature = 68f,
                maxTemperature = 95f,
                humidity = 60
            ),
            visibility = 10000,
            wind = Wind(
                speed = 12.4f,
                direction = 2
            ),
            clouds = Clouds(coverage = 10),
            rain = Rain(
                amount = 2.5f
            ),
            snow = Snow(
                amount = 2.5f
            ),
            dataCalculation = 1781378046,
            sunTime = SunTime(
                country = "US",
                sunRiseTimestamp = 1727353131,
                sunSetTimestamp = 1727396337
            ),
            timeZone = -18000,
            id = 4700168,
            name = "Irving"
        )

        fun getCurrentLocationSuccessResponses() = listOf(
            GeocodeResponse(
                longitude = -96.9489f, latitude = 32.814f, name = "Irving",
                localNames = mapOf("Guadalajara" to "en"), country = "US", state = "Texas"
            )
        )

        fun getZipResponse() = ZipResponse(
            zipCode = "75039",
            name = "Irving",
            longitude = -96.9489f,
            latitude = 32.814f,
            country = "US"
        )

        fun getResponseErrorBody() =
            """{"code": "404", "message": "not found"}""".toResponseBody("application/json".toMediaTypeOrNull())
    }
}