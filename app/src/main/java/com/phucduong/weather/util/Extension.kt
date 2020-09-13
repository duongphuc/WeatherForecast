package com.phucduong.weather.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phucduong.weather.ViewModelFactory
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.remote.model.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun AppCompatActivity.replaceFragment(fragment: Fragment, framId: Int) =
    supportFragmentManager.transact {
        replace(framId, fragment)
    }

fun WeatherResponse.mapToListWeather(keyword: String): List<Weather> {
    val listResult = ArrayList<Weather>()
    for (info in list) {
        val weather = Weather(
            city.name, info.dt.getDate(), info.humidity, info.pressure, info.temp.day.toInt(),
            info.weather[0].description, keyword
        )
        listResult.add(weather)
    }
    return listResult
}

fun Long.getDate(dateFormat: String = "EEE, d MMM yyyy"): String {
    val date = Date(this * 1000)
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) =
    beginTransaction().apply(action).commit()

