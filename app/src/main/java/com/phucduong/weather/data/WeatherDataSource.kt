package com.phucduong.weather.data

interface WeatherDataSource {
    interface LoadWeatherCallBack {
        fun onDataLoaded(listWeather: List<Weather>)
        fun onDataNotAvailable()
    }
    fun getWeatherListByKeyword(keyword: String, callback: LoadWeatherCallBack)
}

interface LocalWeatherDataSource : WeatherDataSource {
    fun saveWeatherList(listWeather: List<Weather>)
}