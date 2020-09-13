package com.phucduong.weather.data

import com.phucduong.weather.data.remote.model.ErrorResponse

class FakeWeatherRepository : WeatherRepository {
    private var cached: LinkedHashMap<String, List<Weather>> = LinkedHashMap()
    override suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>> {
        val list = cached[keyword]
        if (list.isNullOrEmpty()) {
            return Result.Error(ErrorResponse(400, "City not found"))
        }
        return Result.Success(list)
    }

    override suspend fun checkRefreshCached() {

    }

    override fun putDataToCached(keyword: String, listWeather: List<Weather>) {
        cached[keyword] = listWeather
    }
}
