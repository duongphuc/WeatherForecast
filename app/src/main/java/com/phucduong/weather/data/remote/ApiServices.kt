package com.phucduong.weather.data.remote

import com.phucduong.weather.data.remote.model.WeatherResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("forecast/daily")
    fun getWeatherByCity(
        @Query("q") city: String,
        @Query("cnt") numOfDay: Int,
        @Query("units") units: String
    ): Deferred<Response<WeatherResponse>>
}