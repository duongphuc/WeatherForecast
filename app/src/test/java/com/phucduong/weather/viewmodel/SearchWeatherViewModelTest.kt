package com.phucduong.weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.phucduong.weather.data.FakeWeatherRepository
import com.phucduong.weather.data.Weather
import com.phucduong.weather.utils.MainCoroutineRule
import com.phucduong.weather.utils.getOrAwaitValue
import com.phucduong.weather.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
class SearchWeatherViewModelTest {

    // Subject under test
    private lateinit var viewModelTest: SearchWeatherViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeWeatherRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        repository = FakeWeatherRepository()
        val info1 = Weather("Sai Gon", "Datetime1", 10, 10, 10, "Description1", "saigon")
        val info2 = Weather("Sai Gon", "Datetime2", 10, 10, 10, "Description2", "saigon")
        val info3 = Weather("Sai Gon", "Datetime3", 10, 10, 10, "Description3", "saigon")
        repository.putDataToCached("saigon", listOf(info1, info2, info3))
        viewModelTest = SearchWeatherViewModel(repository)
        viewModelTest.searchKeyWord.value = "saigon"
    }

    @Test
    fun getInfoSuccessFromRepositoryAndLoadIntoView() {
        // Load info
        viewModelTest.getWeatherWithCurrentKeyword()
        // Observe the items to keep LiveData emitting
        viewModelTest.listWeather.observeForTesting {

            // Then progress indicator is hidden
            assertThat(viewModelTest.loading.getOrAwaitValue()).isFalse()

            // And data correctly loaded
            assertThat(viewModelTest.listWeather.getOrAwaitValue()).hasSize(3)
        }
    }

    @Test
    fun getInfoErrorFromRepositoryAndErrorShow() {
        repository.setReturnError(true)
        viewModelTest.getWeatherWithCurrentKeyword()
        viewModelTest.listWeather.observeForTesting {
            assertThat(viewModelTest.loading.getOrAwaitValue()).isFalse()
            assertThat(viewModelTest.listWeather.getOrAwaitValue()).isEmpty()
            assertThat(viewModelTest.errorText.getOrAwaitValue()).isNotEmpty()
        }
    }

}