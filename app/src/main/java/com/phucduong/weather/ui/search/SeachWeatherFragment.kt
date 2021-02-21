package com.phucduong.weather.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phucduong.weather.adapter.WeatherAdapter
import com.phucduong.weather.databinding.SearchFragmentBinding
import com.phucduong.weather.viewmodel.SearchWeatherViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchWeatherFragment : Fragment() {
    private lateinit var viewBinding: SearchFragmentBinding
    private val viewModel by sharedViewModel<SearchWeatherViewModel>()
    companion object {
        fun newInstance() = SearchWeatherFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = SearchFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewBinding.viewModel = viewModel
        viewBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
    }

    private fun setupListAdapter() {
        val weatherAdapter = WeatherAdapter(ArrayList(0))
        viewBinding.weatherListInfo.adapter = weatherAdapter
    }
}
