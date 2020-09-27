package com.phucduong.weather.data

interface WeatherDataSource {
    suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>>
}

interface LocalWeatherDataSource {
    suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>>
    suspend fun saveWeatherList(listWeather: List<Weather>)
    suspend fun clearData()
}