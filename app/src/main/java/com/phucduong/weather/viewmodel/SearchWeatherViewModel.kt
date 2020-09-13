package com.phucduong.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phucduong.weather.data.Result
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherRepository
import kotlinx.coroutines.launch
import java.util.*

class SearchWeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    // Two-way databinding, exposing MutableLiveData
    val searchKeyWord = MutableLiveData<String>()
    private val _listWeatherInfo = MutableLiveData<List<Weather>>().apply { value = emptyList() }
    val listWeather: LiveData<List<Weather>>
        get() = _listWeatherInfo

    fun getWeather() {
        viewModelScope.launch {
            val result = weatherRepository.getWeatherListByKeyword(
                searchKeyWord.value?.toLowerCase(
                    Locale.getDefault()
                ) ?: ""
            )
            if (result is Result.Success) {
                _listWeatherInfo.value = result.data
            } else {
                _listWeatherInfo.value = emptyList()
            }
        }
    }
}
