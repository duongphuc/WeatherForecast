package com.phucduong.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.phucduong.weather.data.Weather
import com.phucduong.weather.databinding.WeatherInfoItemBinding

class WeatherAdapter constructor(
    private var listWeather: List<Weather>
) : BaseAdapter() {
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val binding: WeatherInfoItemBinding
        binding = if (view == null) {
            val inflater = LayoutInflater.from(viewGroup.context)
            WeatherInfoItemBinding.inflate(inflater, viewGroup, false)
        } else {
            DataBindingUtil.getBinding(view) ?: throw IllegalAccessException()
        }
        with(binding) {
            info = listWeather[position]
            executePendingBindings()
        }
        return binding.root
    }

    override fun getItem(position: Int) = listWeather[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = listWeather.size

    fun replaceData(listData: List<Weather>) {
        listWeather = listData
        notifyDataSetChanged()
    }
}