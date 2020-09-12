package com.phucduong.weather.data.remote

import com.phucduong.weather.data.Result
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherDataSource

class WeatherRemoteDataSource : WeatherDataSource {
    override suspend fun getWeatherListByKeyword(keyword: String) : Result<List<Weather>> {
        return Result.Success(ArrayList())
    }
}