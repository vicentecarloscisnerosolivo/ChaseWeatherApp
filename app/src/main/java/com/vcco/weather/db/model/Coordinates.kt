package com.vcco.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represent the Coordinates of the requested location
 */
@Entity(tableName = "WeatherCoordinates")
data class Coordinates(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long = 0,
    val longitude: Float,
    val latitude: Float,
)
