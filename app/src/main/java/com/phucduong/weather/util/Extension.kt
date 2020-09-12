package com.phucduong.weather.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phucduong.weather.ViewModelFactory

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass : Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun AppCompatActivity.replaceFragment(fragment : Fragment, framId : Int) =
    supportFragmentManager.transact {
        replace(framId, fragment)
    }

private inline fun FragmentManager.transact(action : FragmentTransaction.() -> Unit) =
    beginTransaction().apply(action).commit()
