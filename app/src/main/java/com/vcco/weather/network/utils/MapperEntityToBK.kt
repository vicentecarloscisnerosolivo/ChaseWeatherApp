package com.vcco.weather.network.utils

import com.vcco.weather.db.relations.CurrentWeatherWithDetails
import com.vcco.weather.model.weather.Clouds
import com.vcco.weather.model.weather.Conditions
import com.vcco.weather.model.weather.Coordinates
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.model.weather.Rain
import com.vcco.weather.model.weather.Snow
import com.vcco.weather.model.weather.SunTime
import com.vcco.weather.model.weather.Temperature
import com.vcco.weather.model.weather.Wind

fun CurrentWeatherWithDetails.toCurrentWeatherResponse() =
    CurrentWeatherResponse(
        clouds =
            Clouds(clouds.coverage),
        coordinates =
            Coordinates(
                longitude = coordinates.longitude,
                latitude = coordinates.latitude,
            ),
        conditions =
            conditions.map {
                Conditions(
                    id = it.id,
                    condition = it.condition,
                    description = it.description,
                    icon = it.icon,
                )
            },
        temperature =
            Temperature(
                temperature = temperature.temperature,
                feelsLike = temperature.feelsLike,
                minTemperature = temperature.minTemperature,
                maxTemperature = temperature.maxTemperature,
                humidity = temperature.humidity,
            ),
        visibility = currentWeather.visibility,
        wind =
            Wind(
                speed = wind.speed,
                direction = wind.direction,
            ),
        rain =
            rain?.let {
                Rain(amount = it.amount)
            },
        snow =
            snow?.let {
                Snow(amount = it.amount)
            },
        dataCalculation = currentWeather.dataCalculation,
        sunTime =
            SunTime(
                country = sunTime.country,
                sunRiseTimestamp = sunTime.sunRiseTimestamp,
                sunSetTimestamp = sunTime.sunSetTimestamp,
            ),
        timeZone = currentWeather.timeZone,
        id = currentWeather.id,
        name = currentWeather.name,
    )
