package com.vcco.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vcco.weather.db.dao.WeatherDao
import com.vcco.weather.db.model.Clouds
import com.vcco.weather.db.model.Conditions
import com.vcco.weather.db.model.Coordinates
import com.vcco.weather.db.model.CurrentWeather
import com.vcco.weather.db.model.Rain
import com.vcco.weather.db.model.Snow
import com.vcco.weather.db.model.SunTime
import com.vcco.weather.db.model.Temperature
import com.vcco.weather.db.model.Wind

@Database(
    entities = [
        CurrentWeather::class,
        Clouds::class,
        Conditions::class,
        Coordinates::class,
        Rain::class,
        Snow::class,
        SunTime::class,
        Temperature::class,
        Wind::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
