package com.phucduong.weather

import android.content.Context
import com.phucduong.weather.data.WeatherRepository

object Injector {
    fun provideWeatherRepository(context : Context) : WeatherRepository {
        return WeatherRepository()
    }
}