package com.vcco.weather.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vcco.weather.db.model.Clouds
import com.vcco.weather.db.model.Conditions
import com.vcco.weather.db.model.Coordinates
import com.vcco.weather.db.model.CurrentWeather
import com.vcco.weather.db.model.Rain
import com.vcco.weather.db.model.Snow
import com.vcco.weather.db.model.SunTime
import com.vcco.weather.db.model.Temperature
import com.vcco.weather.db.model.Wind

@Dao
interface WeatherDao {
    @Transaction
    @Insert
    fun insertNewWeatherAndRemoveOld(
        currentWeather: CurrentWeather,
        deactivateWeather: CurrentWeather? = null,
        clouds: Clouds,
        conditions: List<Conditions>,
        coordinates: Coordinates,
        rain: Rain? = null,
        snow: Snow? = null,
        sunTime: SunTime,
        temperature: Temperature,
        wind: Wind,
    ): Long {
        val parentId = insertWeather(currentWeather)
        insertClouds(clouds.copy(parentId = parentId))
        insertCoordinates(coordinates.copy(parentId = parentId))
        insertConditions(
            conditions.map {
                it.copy(parentId = parentId)
            },
        )
        rain?.let {
            insertRain(it.copy(parentId = parentId))
        }
        snow?.let {
            insertSnow(it.copy(parentId = parentId))
        }
        insertSunTime(sunTime.copy(parentId = parentId))
        insertTemperature(temperature.copy(parentId = parentId))
        insertWind(wind.copy(parentId = parentId))
        deactivateWeather?.let {
            deactivateWeather(deactivateWeather.copy(isActive = false))
        }
        return parentId
    }

    @Insert
    fun insertWeather(currentWeather: CurrentWeather): Long

    @Insert
    fun insertClouds(clouds: Clouds)

    @Insert
    fun insertCoordinates(coordinates: Coordinates)

    @Insert
    fun insertConditions(conditions: List<Conditions>)

    @Insert
    fun insertRain(rain: Rain)

    @Insert
    fun insertSnow(snow: Snow)

    @Insert
    fun insertSunTime(sunTime: SunTime)

    @Insert
    fun insertTemperature(temperature: Temperature)

    @Insert
    fun insertWind(wind: Wind)

    @Transaction
    @Update
    fun deactivateWeather(updateWeather: CurrentWeather)

    @Transaction
    @Query("SELECT * FROM CurrentWeather ORDER BY dataCalculation desc")
    fun getAllWeatherSearched(): MutableList<CurrentWeather>

    @Transaction
    @Query("SELECT * FROM CurrentWeather where isActive = 1 ORDER BY dataCalculation desc")
    fun getLastWeatherSearch(): MutableList<CurrentWeather>

    @Query("SELECT * FROM CurrentWeather ORDER BY dataCalculation desc LIMIT 1")
    fun getLastWeatherSearched(): CurrentWeather?

    @Query("SELECT * FROM WEATHERCLOUDS WHERE parentId = :parentId")
    fun getCloudsForWeather(parentId: Long): Clouds

    @Query("SELECT * FROM WeatherConditions WHERE parentId = :parentId")
    fun getConditionForWeather(parentId: Long): List<Conditions>

    @Query("SELECT * FROM WeatherCoordinates WHERE parentId = :parentId")
    fun getCoordinatesForWeather(parentId: Long): Coordinates

    @Query("SELECT * FROM WeatherRain WHERE parentId = :parentId")
    fun getRainForWeather(parentId: Long): Rain?

    @Query("SELECT * FROM WEATHERSNOW WHERE parentId = :parentId")
    fun getSnowForWeather(parentId: Long): Snow?

    @Query("SELECT * FROM WeatherSunTime WHERE parentId = :parentId")
    fun getSunTimeForWeather(parentId: Long): SunTime

    @Query("SELECT * FROM WeatherTemperature WHERE parentId = :parentId")
    fun getTemperatureForWeather(parentId: Long): Temperature

    @Query("SELECT * FROM WeatherWind WHERE parentId = :parentId")
    fun getWindForWeather(parentId: Long): Wind
}
