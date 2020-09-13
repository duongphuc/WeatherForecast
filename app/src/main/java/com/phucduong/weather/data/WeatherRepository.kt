package com.phucduong.weather.data

class WeatherRepository constructor(
    private val localDataSource: LocalWeatherDataSource,
    private val remoteDataSource: WeatherDataSource
) {
    var cached: LinkedHashMap<String, List<Weather>> = LinkedHashMap()
    var refreshCached = false

    suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>> {
        if (cached.isNotEmpty() && !refreshCached) {
            val listWeather = cached[keyword]
            if (!listWeather.isNullOrEmpty()) {
                return Result.Success(listWeather)
            }
        }

        if (!refreshCached) {
            val result = localDataSource.getWeatherListByKeyword(keyword)
            if (result is Result.Success) {
                putDataToCached(keyword, result.data)
                return result
            }
        }
        return getWeatherListFromRemote(keyword)
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
    }
}
