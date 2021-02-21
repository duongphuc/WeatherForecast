package com.phucduong.weather.data.local

import android.os.HandlerThread
import com.phucduong.weather.data.LocalWeatherDataSource
import com.phucduong.weather.data.Result
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.remote.model.ErrorResponse
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

    override suspend fun clearData() {
        withContext(ioDispatcher) {
            weatherDao.clearData()
        }
    }

    override suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val listWeather = weatherDao.getWeatherByKeyWord(keyword)
                if (listWeather.isNullOrEmpty()) {
                    Result.Error(ErrorResponse(0, "Data not available"))
                } else {
                    Result.Success(weatherDao.getWeatherByKeyWord(keyword))
                }
            } catch (e: Exception) {
                Result.UnKnowError(e)
            }
        }
}