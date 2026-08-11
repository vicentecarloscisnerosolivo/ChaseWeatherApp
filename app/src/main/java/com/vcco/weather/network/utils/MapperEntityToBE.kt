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
        clouds = cloudsToCloudsResponse(),
        coordinates = coordinatesToCoordinatesResponse(),
        conditions = conditionsListToConditionsResponse(),
        base = currentWeather.base,
        temperature = temperatureToTemperatureResponse(),
        visibility = currentWeather.visibility,
        wind = windToWindResponse(),
        rain = rainToRainResponse(),
        snow = snotToSnowResponse(),
        dataCalculation = currentWeather.dataCalculation,
        sunTime = sunTimeToSunTimeResponse(),
        timeZone = currentWeather.timeZone,
        id = currentWeather.id,
        name = currentWeather.name,
    )

fun CurrentWeatherWithDetails.cloudsToCloudsResponse() =
    Clouds(
        coverage = clouds.coverage,
    )

fun CurrentWeatherWithDetails.coordinatesToCoordinatesResponse() =
    Coordinates(
        latitude = coordinates.latitude,
        longitude = coordinates.longitude,
    )

fun CurrentWeatherWithDetails.conditionsListToConditionsResponse() =
    conditions.map { condition ->
        Conditions(
            id = condition.id,
            condition = condition.condition,
            description = condition.description,
            icon = condition.icon,
        )
    }

fun CurrentWeatherWithDetails.temperatureToTemperatureResponse() =
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

fun CurrentWeatherWithDetails.windToWindResponse() =
    Wind(
        speed = wind.speed,
        direction = wind.direction,
        gust = wind.gust,
    )

fun CurrentWeatherWithDetails.rainToRainResponse() =
    rain?.let {
        Rain(
            amount = it.amount,
        )
    }

fun CurrentWeatherWithDetails.snotToSnowResponse() =
    snow?.let {
        Snow(
            amount = it.amount,
        )
    }

fun CurrentWeatherWithDetails.sunTimeToSunTimeResponse() =
    SunTime(
        country = sunTime.country,
        sunRiseTimestamp = sunTime.sunRiseTimestamp,
        sunSetTimestamp = sunTime.sunSetTimestamp,
    )
