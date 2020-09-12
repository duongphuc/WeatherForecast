package com.phucduong.weather.data.local

import com.phucduong.weather.data.LocalWeatherDataSource
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherDataSource
import com.phucduong.weather.util.AppExecutors

class WeatherLocalDataSource(
    val executor: AppExecutors,
    val weatherDao: WeatherDao
) : LocalWeatherDataSource {
    override fun saveWeatherList(listWeather: List<Weather>) {
        executor.diskIO.execute {
            weatherDao.insertWeather(listWeather)
        }
    }

    override fun getWeatherListByKeyword(keyword: String, callback: WeatherDataSource.LoadWeatherCallBack) {
        executor.diskIO.execute {
            val listWeather = weatherDao.getWeatherByKeyWord(keyword)
            executor.mainThread.execute {
                if (listWeather.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onDataLoaded(listWeather)
                }
            }
        }
    }
}