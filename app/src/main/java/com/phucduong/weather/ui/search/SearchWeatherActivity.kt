package com.phucduong.weather.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phucduong.weather.R
import com.phucduong.weather.ui.main.search.SeachWeatherFragment
import com.phucduong.weather.util.obtainViewModel
import com.phucduong.weather.util.replaceFragment
import com.phucduong.weather.viewmodel.SearchWeatherViewModel

class SearchWeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        replaceFragment(SeachWeatherFragment.newInstance(), R.id.container)
    }

    fun obtainViewModel(): SearchWeatherViewModel = obtainViewModel(SearchWeatherViewModel::class.java)

}
