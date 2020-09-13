package com.phucduong.weather.util

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ListView
import androidx.databinding.BindingAdapter
import com.phucduong.weather.adapter.WeatherAdapter
import com.phucduong.weather.data.Weather


object BindingUtils {
    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(listView: ListView, list: List<Weather>) {
        with(listView.adapter as WeatherAdapter) {
            replaceData(list)
            if (list.isNotEmpty()) listView.context.hideKeyboard(listView)
        }
    }

    @JvmStatic
    @BindingAdapter("onEditorEnterAction")
    fun EditText.onEditorEnterAction(performSearch: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
            }
            true
        }
    }
}