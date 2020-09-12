package com.phucduong.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherDataSource
import com.phucduong.weather.data.WeatherRepository

class SearchWeatherViewModel(
    private val weatherRepository: WeatherRepository
): ViewModel() {
    // Two-way databinding, exposing MutableLiveData
    val searchKeyWord = MutableLiveData<String>()
    private val _listWeatherInfo = MutableLiveData<List<Weather>>().apply { value = emptyList() }
    val listWeather: LiveData<List<Weather>>
        get() = _listWeatherInfo

    fun getWeather() {
        weatherRepository.getWeatherListByKeyword(searchKeyWord.value?:"", object : WeatherDataSource.LoadWeatherCallBack {
            override fun onDataLoaded(listWeather: List<Weather>) {
                _listWeatherInfo.value = listWeather
            }

            override fun onDataNotAvailable() {

            }
        })
    }
}
