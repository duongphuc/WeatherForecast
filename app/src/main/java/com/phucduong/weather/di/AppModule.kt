package com.phucduong.weather.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.phucduong.weather.BuildConfig
import com.phucduong.weather.Constant
import com.phucduong.weather.data.DefaultWeatherRepository
import com.phucduong.weather.data.LocalWeatherDataSource
import com.phucduong.weather.data.WeatherDataSource
import com.phucduong.weather.data.WeatherRepository
import com.phucduong.weather.data.local.WeatherDatabase
import com.phucduong.weather.data.local.WeatherLocalDataSource
import com.phucduong.weather.data.remote.ApiServices
import com.phucduong.weather.data.remote.WeatherRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesWeatherRemoteDataSource(apiServices: ApiServices) : WeatherDataSource {
        return WeatherRemoteDataSource(apiServices)
    }

    @Provides
    @Singleton
    fun providesApiServices(retrofit: Retrofit) : ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofit(httpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(authInterceptor: Interceptor) : OkHttpClient {
        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun providesAuthInterceptor() = Interceptor { chain ->
            val newUrl =
                chain.request().url().newBuilder().addQueryParameter("appId", Constant.WEATHER_APP_ID)
                    .build()
            val newRequest = chain.request().newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
    }

    @Provides
    fun provideLocalDataSource(database: WeatherDatabase, ioDispatcher: CoroutineDispatcher): LocalWeatherDataSource {
        return WeatherLocalDataSource(database.weatherDao(), ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "Weather.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context) = context
                .getSharedPreferences(Constant.PREFERENCES, MODE_PRIVATE)
}

@Module
@InstallIn(ApplicationComponent::class)
object WeatherRepositoryModule {
    @Singleton
    @Provides
    fun provideWeatherRepository(localDataSource: LocalWeatherDataSource, remoteDataSource: WeatherDataSource, sharedPreferences: SharedPreferences): WeatherRepository {
        return DefaultWeatherRepository(localDataSource, remoteDataSource, sharedPreferences)
    }
}