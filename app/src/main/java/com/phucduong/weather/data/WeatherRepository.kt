package com.phucduong.weather.data

import com.phucduong.weather.data.local.WeatherLocalDataSource

class WeatherRepository constructor(
    val localDataSource: WeatherLocalDataSource,
    val remoteDataSource: WeatherDataSource
) {
    var cached: LinkedHashMap<String, List<Weather>> = LinkedHashMap()
    var refreshCached = false
    fun getWeatherListByKeyword(keyword: String, callback: WeatherDataSource.LoadWeatherCallBack) {
        if (cached.isNotEmpty() && !refreshCached) {
            val listWeather = cached[keyword]
            if (!listWeather.isNullOrEmpty()) {
                callback.onDataLoaded(listWeather)
                return
            }
        }

        if (!refreshCached) {
            localDataSource.getWeatherListByKeyword(keyword, object: WeatherDataSource.LoadWeatherCallBack {
                override fun onDataLoaded(listWeather: List<Weather>) {
                    callback.onDataLoaded(listWeather)
                }

                override fun onDataNotAvailable() {
                    getWeatherListFromRemote(keyword, callback)
                }
            })
        } else {
            getWeatherListFromRemote(keyword, callback)
        }
    }

    fun getWeatherListFromRemote(keyword: String, callback: WeatherDataSource.LoadWeatherCallBack) {
        remoteDataSource.getWeatherListByKeyword(keyword, object: WeatherDataSource.LoadWeatherCallBack {
            override fun onDataLoaded(listWeather: List<Weather>) {
                putDataToCached(keyword, listWeather)
                putDataToLocal(listWeather)
                callback.onDataLoaded(listWeather)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })
    }

    fun putDataToCached(keyword: String, listWeather: List<Weather>) {
        cached.put(keyword, listWeather)
    }

    fun putDataToLocal(listWeather: List<Weather>) {
        localDataSource.saveWeatherList(listWeather)
    }
}
