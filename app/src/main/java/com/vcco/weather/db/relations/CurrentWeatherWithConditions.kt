package com.vcco.weather.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vcco.weather.db.model.Conditions
import com.vcco.weather.db.model.CurrentWeather

data class CurrentWeatherWithConditions(
    @Embedded val currentWeather: CurrentWeather,
    @Relation(
        parentColumn = "uId",
        entityColumn = "parentId",
    )
    val conditions: List<Conditions>,
)
