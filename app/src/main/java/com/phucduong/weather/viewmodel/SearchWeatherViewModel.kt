package com.phucduong.weather.viewmodel

import androidx.arch.core.util.Function
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.phucduong.weather.data.Result
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherRepository
import kotlinx.coroutines.launch
import java.util.*

class SearchWeatherViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    // Two-way databinding, exposing MutableLiveData
    val searchKeyWord = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val errorText = MutableLiveData<String>()

    private val _listWeatherInfo = MutableLiveData<List<Weather>>().apply { value = emptyList() }
    val listWeather: LiveData<List<Weather>>
        get() = _listWeatherInfo

    //Used below code for observe searchKeyword value change
    private val mediator = MediatorLiveData<String>().apply {
        addSource(searchKeyWord) { value ->
            setValue(value)
            //getWeatherWithCurrentKeyword()
        }
    }.also { it.observeForever() { /* empty */ } }

    fun getWeatherWithCurrentKeyword() {
        loading.value = true
        viewModelScope.launch {
            val result = weatherRepository.getWeatherListByKeyword(
                searchKeyWord.value?.trim()?.toLowerCase(
                    Locale.getDefault()
                ) ?: ""
            )
            loading.value = false
            when (result) {
                is Result.Success -> bindData(result.data, "")
                is Result.Error -> bindData(emptyList(), result.errorResponse?.message)
                is Result.NetWorkError -> bindData(emptyList(), result.msg)
                is Result.UnKnowError -> bindData(emptyList(), result.msg)
            }
        }
    }

    private fun bindData(weatherList: List<Weather>, errorMsg: String?) {
        _listWeatherInfo.value = weatherList
        errorText.value = errorMsg
    }

    fun checkRefreshCached() {
        viewModelScope.launch {
            weatherRepository.checkRefreshCached()
        }
    }
}
