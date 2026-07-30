package com.vcco.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represent current Conditions of the requested Location
 * ie: Cloudy, Rainy, Clear
 */
@Entity(tableName = "WeatherConditions")
data class Conditions(
    @PrimaryKey(autoGenerate = true)
    val uId: Long = 0,
    val parentId: Long = 0,
    val id: Int,
    val condition: String,
    val description: String,
    val icon: String,
)
