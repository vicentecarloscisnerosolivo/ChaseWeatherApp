package com.vcco.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CurrentWeather")
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true)
    val uId: Long = 0,
    val visibility: Int,
    val base: String? = null,
    val dataCalculation: Long,
    val timeZone: Long,
    val id: Int,
    val name: String,
    val isActive: Boolean = true,
)
