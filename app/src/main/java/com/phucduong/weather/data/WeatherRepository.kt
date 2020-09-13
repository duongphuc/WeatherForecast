package com.phucduong.weather.data

import android.content.SharedPreferences
import android.text.format.DateUtils

class WeatherRepository constructor(
    private val localDataSource: LocalWeatherDataSource,
    private val remoteDataSource: WeatherDataSource,
    private val sharedPreferences: SharedPreferences
) {
    private var cached: LinkedHashMap<String, List<Weather>> = LinkedHashMap()

    suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>> {
        if (cached.isNotEmpty()) {
            val listWeather = cached[keyword]
            if (!listWeather.isNullOrEmpty()) {
                return Result.Success(listWeather)
            }
        }

        val result = localDataSource.getWeatherListByKeyword(keyword)
        if (result is Result.Success) {
            putDataToCached(keyword, result.data)
            return result
        }
        return getWeatherListFromRemote(keyword)
    }

    suspend fun checkRefreshCached() {
        val lastTimeCached = sharedPreferences.getLong("cachedTime", System.currentTimeMillis())
        if (!DateUtils.isToday(lastTimeCached)) {
            clearLocalData()
        }
    }

    private suspend fun getWeatherListFromRemote(keyword: String): Result<List<Weather>> {
        val result = remoteDataSource.getWeatherListByKeyword(keyword)
        if (result is Result.Success) {
            putDataToCached(keyword, result.data)
            putDataToLocal(result.data)
        }
        return result
    }

    private fun putDataToCached(keyword: String, listWeather: List<Weather>) {
        cached[keyword] = listWeather
    }

    private suspend fun putDataToLocal(listWeather: List<Weather>) {
        localDataSource.saveWeatherList(listWeather)
        setCacheLocalTime()
    }

    private fun setCacheLocalTime() {
        val editor = sharedPreferences.edit()
        editor.putLong("cachedTime", System.currentTimeMillis())
        editor.apply()
    }

    private fun clearCachedTime() {
        val editor = sharedPreferences.edit()
        editor.remove("cachedTime")
        editor.apply()
    }

    private suspend fun clearLocalData() {
        localDataSource.clearData()
        cached.clear()
        clearCachedTime()
    }
}
