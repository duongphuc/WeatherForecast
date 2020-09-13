package com.phucduong.weather.data

interface WeatherRepository {
    suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>>
    suspend fun checkRefreshCached()
    fun putDataToCached(keyword: String, listWeather: List<Weather>)
}