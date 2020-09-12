package com.phucduong.weather.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phucduong.weather.R
import com.phucduong.weather.ui.main.search.SeachWeatherFragment

class SearchWeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SeachWeatherFragment.newInstance())
                .commitNow()
        }
    }

}
