package com.vcco.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Country code and expected sun rise and sun set in the requested location
 */
@Entity(tableName = "WeatherSunTime")
data class SunTime(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long = 0,
    val country: String,
    val sunRiseTimestamp: Long,
    val sunSetTimestamp: Long,
)
