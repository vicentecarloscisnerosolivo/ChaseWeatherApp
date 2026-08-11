package com.vcco.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Current Wind conditions in the location
 */
@Entity(tableName = "WeatherWind")
data class Wind(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long = 0,
    val speed: Float,
    val direction: Int,
    val gust: Float? = null,
)
