package com.phucduong.weather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phucduong.weather.data.Weather

@Dao
interface WeatherDao {
    @Query("SELECT * FROM Weather WHERE keyword = :keyword")
    suspend fun getWeatherByKeyWord(keyword: String): List<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(listWeather: List<Weather>)

    @Query("DELETE FROM weather")
    fun clearData()
}