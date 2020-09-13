package com.phucduong.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phucduong.weather.data.Result
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherRepository
import kotlinx.coroutines.launch
import java.lang.Error
import java.util.*

class SearchWeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    // Two-way databinding, exposing MutableLiveData
    val searchKeyWord = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val errorText = MutableLiveData<String>()

    private val _listWeatherInfo = MutableLiveData<List<Weather>>().apply { value = emptyList() }
    val listWeather: LiveData<List<Weather>>
        get() = _listWeatherInfo

    fun getWeather() {
        loading.value = true
        viewModelScope.launch {
            val result = weatherRepository.getWeatherListByKeyword(
                searchKeyWord.value?.trim()?.toLowerCase(
                    Locale.getDefault()
                ) ?: ""
            )
            loading.value = false
            when (result) {
                is Result.Success -> {
                    bindData(result.data, "")
                }
                is Result.Error -> {
                    bindData(emptyList(), result.exception.message)
                }
            }
        }
    }

    private fun bindData(weartherList: List<Weather>, errorMsg: String?) {
        _listWeatherInfo.value = weartherList
        errorText.value = errorMsg
    }
}
