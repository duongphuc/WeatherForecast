package com.phucduong.weather.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.room.Room
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
import com.phucduong.weather.viewmodel.SearchWeatherViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    viewModel { SearchWeatherViewModel(get()) }
}

val retrofitModule = module {
    single { providesAuthInterceptor() }
    single { provideOkHttp(get()) }
    single { providesRetrofit(get()) }
    single<ApiServices> { get<Retrofit>().create(ApiServices::class.java) }
}

val dbModule = module {
    single { provideDataBase(get()) }
    single { get<WeatherDatabase>().weatherDao() }
}

val dataSourceModule = module {
    single<WeatherDataSource> { WeatherRemoteDataSource(get()) }
    single { Dispatchers.IO }
    single<LocalWeatherDataSource> { WeatherLocalDataSource(get(), get()) }
    single<WeatherRepository> { DefaultWeatherRepository(get(), get(), get()) }
}

val sharedModule = module {
    single { get<Context>().getSharedPreferences(Constant.PREFERENCES, MODE_PRIVATE) }
}

fun provideOkHttp(authInterceptor: Interceptor): OkHttpClient {
    val okHttpClientBuilder = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
    if (BuildConfig.DEBUG) {
        okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }
    return okHttpClientBuilder.build()
}

fun providesAuthInterceptor() = Interceptor { chain ->
    val newUrl =
        chain.request().url().newBuilder().addQueryParameter("appId", Constant.WEATHER_APP_ID)
            .build()
    val newRequest = chain.request().newBuilder().url(newUrl).build()
    chain.proceed(newRequest)
}

fun providesRetrofit(httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(httpClient)
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

fun provideDataBase(context: Context): WeatherDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        WeatherDatabase::class.java,
        Constant.DB_NAME
    ).build()
}