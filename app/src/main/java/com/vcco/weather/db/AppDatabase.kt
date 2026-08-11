package com.vcco.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE CurrentWeather ADD COLUMN base TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE WeatherTemperature ADD COLUMN pressure INTEGER")
        db.execSQL("ALTER TABLE WeatherTemperature ADD COLUMN seaLevel INTEGER")
        db.execSQL("ALTER TABLE WeatherTemperature ADD COLUMN groundLevel INTEGER")
        db.execSQL("ALTER TABLE WeatherWind ADD COLUMN gust REAL")
    }
}
