package com.phucduong.weather

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.phucduong.weather.data.DefaultWeatherRepository
import com.phucduong.weather.data.WeatherRepository
import com.phucduong.weather.data.local.WeatherDatabase
import com.phucduong.weather.data.local.WeatherLocalDataSource
import com.phucduong.weather.data.remote.ApiServices
import com.phucduong.weather.data.remote.WeatherRemoteDataSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Injector {
    fun provideWeatherRepository(context: Context): WeatherRepository {
        val db = WeatherDatabase.getInstance(context);
        return DefaultWeatherRepository(
            WeatherLocalDataSource(db.weatherDao()),
            WeatherRemoteDataSource(apiServices),
            context.getSharedPreferences(Constant.PREFERENCES, Context.MODE_PRIVATE)
        )
    }

    private val authInterceptor = Interceptor { chain ->
        val newUrl =
            chain.request().url().newBuilder().addQueryParameter("appId", Constant.WEATHER_APP_ID)
                .build()
        val newRequest = chain.request().newBuilder().url(newUrl).build()
        chain.proceed(newRequest)
    }

    private val httpLogging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient =
        OkHttpClient().newBuilder().addInterceptor(authInterceptor).addInterceptor(
            httpLogging
        ).build()

    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val apiServices = retrofit().create(ApiServices::class.java)
}