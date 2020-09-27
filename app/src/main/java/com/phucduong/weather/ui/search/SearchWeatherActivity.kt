package com.phucduong.weather.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phucduong.weather.R
import com.phucduong.weather.ui.main.search.SearchWeatherFragment
import com.phucduong.weather.util.replaceFragment
import com.phucduong.weather.viewmodel.SearchWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchWeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        replaceFragment(SearchWeatherFragment.newInstance(), R.id.container)
    }
}
