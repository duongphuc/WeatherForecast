package com.phucduong.weather.ui.main.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phucduong.weather.R
import com.phucduong.weather.viewmodel.SearchWeatherViewModel

class SeachWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = SeachWeatherFragment()
    }

    private lateinit var viewModel: SearchWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
