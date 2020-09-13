package com.phucduong.weather.data.remote.model

data class WeatherResponse(
    val city: City,
    val list: List<WeatherInfo>
)

data class City(
    val name: String
)

data class WeatherInfo(
    val dt: Long,
    val humidity: Int,
    val pressure: Int,
    val temp: Temp,
    val weather: List<Weather>
)

data class Weather(
    val description: String
)


data class Temp(
    val day: Double
)