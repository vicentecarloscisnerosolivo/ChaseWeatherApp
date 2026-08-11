package com.vcco.weather.network.utils

import com.vcco.weather.db.model.Clouds
import com.vcco.weather.db.model.Conditions
import com.vcco.weather.db.model.Coordinates
import com.vcco.weather.db.model.CurrentWeather
import com.vcco.weather.db.model.Rain
import com.vcco.weather.db.model.Snow
import com.vcco.weather.db.model.SunTime
import com.vcco.weather.db.model.Temperature
import com.vcco.weather.db.model.Wind
import com.vcco.weather.model.weather.CurrentWeatherResponse

fun CurrentWeatherResponse.toCurrentWeather() =
    CurrentWeather(
        base = base,
        visibility = visibility,
        dataCalculation = dataCalculation,
        timeZone = timeZone,
        id = id,
        name = name,
    )

fun CurrentWeatherResponse.toCurrentClouds() =
    Clouds(
        coverage = clouds.coverage,
    )

fun CurrentWeatherResponse.toConditions() =
    conditions.map { condition ->
        Conditions(
            id = condition.id,
            condition = condition.condition,
            description = condition.description,
            icon = condition.icon,
        )
    }

fun CurrentWeatherResponse.toCoordinates() =
    Coordinates(
        longitude = coordinates.longitude,
        latitude = coordinates.latitude,
    )

fun CurrentWeatherResponse.toRain() =
    rain?.let { rain ->
        Rain(
            amount = rain.amount,
        )
    }

fun CurrentWeatherResponse.toSnow() =
    snow?.let { snow ->
        Snow(
            amount = snow.amount,
        )
    }

fun CurrentWeatherResponse.toSunTime() =
    SunTime(
        country = sunTime.country,
        sunRiseTimestamp = sunTime.sunRiseTimestamp,
        sunSetTimestamp = sunTime.sunSetTimestamp,
    )

fun CurrentWeatherResponse.toTemperature() =
    Temperature(
        temperature = temperature.temperature,
        feelsLike = temperature.feelsLike,
        minTemperature = temperature.minTemperature,
        maxTemperature = temperature.maxTemperature,
        humidity = temperature.humidity,
        pressure = temperature.pressure,
        seaLevel = temperature.seaLevel,
        groundLevel = temperature.groundLevel,
    )

fun CurrentWeatherResponse.toWind() =
    Wind(
        speed = wind.speed,
        direction = wind.direction,
        gust = wind.gust,
    )
