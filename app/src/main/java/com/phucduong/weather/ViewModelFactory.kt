package com.phucduong.weather

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phucduong.weather.data.DefaultWeatherRepository
import com.phucduong.weather.data.WeatherRepository
import com.phucduong.weather.viewmodel.SearchWeatherViewModel

class ViewModelFactory private constructor(
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(SearchWeatherViewModel::class.java) -> SearchWeatherViewModel(
                    weatherRepository
                )
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE
                    ?: ViewModelFactory(Injector.provideWeatherRepository(application.applicationContext))
                        .also { INSTANCE = it }
            }
    }
}