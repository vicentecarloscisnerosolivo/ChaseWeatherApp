package com.vcco.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Shows Cloud coverage in the requested location
 */
@Entity(tableName = "WeatherClouds")
data class Clouds(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long = 0,
    val coverage: Int,
)
