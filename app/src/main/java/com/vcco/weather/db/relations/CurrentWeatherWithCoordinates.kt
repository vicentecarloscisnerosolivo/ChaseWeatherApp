package com.vcco.weather.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vcco.weather.db.model.Coordinates
import com.vcco.weather.db.model.CurrentWeather

data class CurrentWeatherWithCoordinates(
    @Embedded val currentWeather: CurrentWeather,
    @Relation(
        parentColumn = "uId",
        entityColumn = "parentId",
    )
    val coordinates: Coordinates,
)
