package com.phucduong.weather

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.phucduong.weather.data.FakeWeatherRepository
import com.phucduong.weather.data.WeatherRepository

object Injector {
    var weatherRepository: WeatherRepository? = null
        @VisibleForTesting set
    fun provideWeatherRepository(context: Context): WeatherRepository {
        return weatherRepository ?: FakeWeatherRepository()
    }
}