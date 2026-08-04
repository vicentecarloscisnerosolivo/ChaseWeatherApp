package com.vcco.weather.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vcco.weather.db.model.Clouds
import com.vcco.weather.db.model.Conditions
import com.vcco.weather.db.model.Coordinates
import com.vcco.weather.db.model.CurrentWeather
import com.vcco.weather.db.model.Rain
import com.vcco.weather.db.model.Snow
import com.vcco.weather.db.model.SunTime
import com.vcco.weather.db.model.Temperature
import com.vcco.weather.db.model.Wind

data class CurrentWeatherWithDetails(
    @Embedded
    val currentWeather: CurrentWeather,
    @Relation(
        entity = Clouds::class,
        entityColumn = "parentId",
        parentColumn = "uId",
    )
    val clouds: Clouds,
    @Relation(
        entity = Conditions::class,
        entityColumn = "parentId",
        parentColumn = "uId",
    )
    val conditions: List<Conditions>,
    @Relation(
        entity = Coordinates::class,
        entityColumn = "parentId",
        parentColumn = "uId",
    )
    val coordinates: Coordinates,
    @Relation(
        entity = Rain::class,
        entityColumn = "parentId",
        parentColumn = "uId",
    )
    val rain: Rain? = null,
    @Relation(
        entity = Snow::class,
        entityColumn = "parentId",
        parentColumn = "uId",
    )
    val snow: Snow? = null,
    @Relation(
        entity = SunTime::class,
        entityColumn = "parentId",
        parentColumn = "uId",
    )
    val sunTime: SunTime,
    @Relation(
        entity = Temperature::class,
        entityColumn = "parentId",
        parentColumn = "uId",
    )
    val temperature: Temperature,
    @Relation(
        entity = Wind::class,
        entityColumn = "parentId",
        parentColumn = "uId",
    )
    val wind: Wind,
)
