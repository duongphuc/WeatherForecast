package com.phucduong.weather.data

interface WeatherDataSource {
    suspend fun getWeatherListByKeyword(keyword: String) : Result<List<Weather>>
}

interface LocalWeatherDataSource : WeatherDataSource {
    suspend fun saveWeatherList(listWeather: List<Weather>)
}