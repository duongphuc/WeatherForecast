package com.phucduong.weather.data.remote

import android.util.Log
import com.phucduong.weather.Constant
import com.phucduong.weather.data.Result
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherDataSource
import com.phucduong.weather.util.mapToListWeather
import retrofit2.Response
import java.io.IOException

class WeatherRemoteDataSource constructor(private val apiServices: ApiServices) :
    WeatherDataSource {
    override suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>> {
        val weatherResponse = safeApiCall({
            apiServices.getWeatherByCity(
                keyword,
                Constant.DEFAULT_NUMBER_OF_DAY,
                Constant.DEFAULT_UNITS
            ).await()
        }, errorMessage = "Data not found")
        val listMappedWeather = weatherResponse?.mapToListWeather(keyword)
        if (listMappedWeather.isNullOrEmpty()) {
            return Result.Error(Exception("Data not found"))
        }
        return Result.Success(listMappedWeather!!)
    }

    private suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): T? {

        val result: Result<T> = safeApiResult(call, errorMessage)
        var data: T? = null

        when (result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }
        return data
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}