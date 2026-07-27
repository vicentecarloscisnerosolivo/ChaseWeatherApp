package com.vcco.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Snow expected in the current requested location, can be a null value
 *
 *  NOTE: Value given in mm/h from API
 */
@Entity(tableName = "WeatherSnow")
data class Snow(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long = 0,
    val amount: Float,
)
