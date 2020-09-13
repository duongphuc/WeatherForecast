package com.phucduong.weather.data.remote

import android.util.Log
import com.phucduong.weather.Constant
import com.phucduong.weather.data.Result
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherDataSource
import com.phucduong.weather.data.remote.model.ErrorResponse
import com.phucduong.weather.util.mapToListWeather
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.IllegalArgumentException

class WeatherRemoteDataSource constructor(private val apiServices: ApiServices) :
    WeatherDataSource {
    override suspend fun getWeatherListByKeyword(keyword: String): Result<List<Weather>> {
        val weatherResponseResult = safeApiResult({
            apiServices.getWeatherByCity(
                keyword,
                Constant.DEFAULT_NUMBER_OF_DAY,
                Constant.DEFAULT_UNITS
            ).await()
        }, errorMessage = "Sorry something when wrong")

        when (weatherResponseResult) {
            is Result.Success -> {
                val weatherResponse = weatherResponseResult.data
                val listMappedWeather = weatherResponse.mapToListWeather(keyword)
                if (listMappedWeather.isNullOrEmpty()) {
                    return Result.Error(Exception("Data not found"))
                }
                return Result.Success(listMappedWeather)
            }
            is Result.Error -> return weatherResponseResult
            is Result.NetWorkError -> return weatherResponseResult
        }
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) return Result.Success(response.body()!!)
            val errorResponse = convertErrorBody(response.errorBody())
            Log.d("Error response: ", errorMessage)
        } catch (e : Throwable) {
            when (e) {
                is IOException -> Result.NetWorkError
                is HttpException -> {
                    val code = e.code()
                    Result.Error(e)
                }
                else -> {
                    Result.Error(Exception())
                }
            }
        }
        return Result.Error(IOException())
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorResponse? {
        return try {
            errorBody?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}