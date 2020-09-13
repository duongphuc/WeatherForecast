package com.phucduong.weather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.phucduong.weather.data.Weather

@Database(entities = [Weather::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        private var INSTANCE: WeatherDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): WeatherDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java, "Weather.db"
                    )
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}