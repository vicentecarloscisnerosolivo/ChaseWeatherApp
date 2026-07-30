package com.vcco.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represent the current Temperature conditions on the requested location
 * could be given in metric (°C) or imperial values (°F)
 *
 * DEFAULT: Configured for Metric values
 */
@Entity(tableName = "WeatherTemperature")
data class Temperature(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long = 0,
    val temperature: Float,
    val feelsLike: Float,
    val minTemperature: Float,
    val maxTemperature: Float,
    val humidity: Int,
)
