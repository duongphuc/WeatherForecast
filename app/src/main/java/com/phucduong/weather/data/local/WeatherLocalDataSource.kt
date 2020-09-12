package com.phucduong.weather.data.local

import com.phucduong.weather.data.LocalWeatherDataSource
import com.phucduong.weather.data.Result
import com.phucduong.weather.data.Weather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherLocalDataSource(
    private val weatherDao: WeatherDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalWeatherDataSource {
    override suspend fun saveWeatherList(listWeather: List<Weather>) = withContext(ioDispatcher) {
        weatherDao.insertWeather(listWeather)
    }

    override suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(weatherDao.getWeatherByKeyWord(keyword))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}