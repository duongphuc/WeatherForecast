package com.phucduong.weather

import android.content.Context
import com.phucduong.weather.data.LocalWeatherDataSource
import com.phucduong.weather.data.WeatherRepository
import com.phucduong.weather.data.local.WeatherDatabase
import com.phucduong.weather.data.local.WeatherLocalDataSource
import com.phucduong.weather.data.remote.WeatherRemoteDataSource
import com.phucduong.weather.util.AppExecutors

object Injector {
    fun provideWeatherRepository(context : Context) : WeatherRepository {
        val db = WeatherDatabase.getInstance(context);
        return WeatherRepository(WeatherLocalDataSource(AppExecutors(), db.weatherDao()), WeatherRemoteDataSource())
    }
}