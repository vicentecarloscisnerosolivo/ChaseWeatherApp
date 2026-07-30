package com.vcco.weather.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vcco.weather.db.model.CurrentWeather
import com.vcco.weather.db.model.Rain

data class CurrentWeatherWithRain(
    @Embedded
    val currentWeather: CurrentWeather,
    @Relation(
        parentColumn = "uId",
        entityColumn = "parentId",
    )
    val rain: Rain? = null,
)
