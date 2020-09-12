package com.phucduong.weather.util

import android.widget.ListView
import androidx.databinding.BindingAdapter
import com.phucduong.weather.adapter.WeatherAdapter
import com.phucduong.weather.data.Weather

object BindingUtils {
    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: ListView, list: List<Weather>) {
        with(listView.adapter as WeatherAdapter) {
            replaceData(list)
        }
    }
}