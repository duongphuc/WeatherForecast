package com.phucduong.weather.ui.search

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.phucduong.weather.Injector
import com.phucduong.weather.R
import com.phucduong.weather.data.FakeWeatherRepository
import com.phucduong.weather.data.Weather
import com.phucduong.weather.data.WeatherRepository
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.IsAnything.anything
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SearchWeatherFragmentTest {
    private lateinit var repository: WeatherRepository

    @Before
    fun initRepository() {
        repository = FakeWeatherRepository()
        Injector.weatherRepository = repository
    }

    @Test
    fun test01_searchAvailableKeywordOnCached_DisplayDataOnUI() {
        // GIVEN - Add keyword list weather to cache
        val weather = Weather("sai gon", "Wed, 16 Sep 2020", 67, 1010, 32,  "moderate rain", "saigon")
        repository.putDataToCached("saigon", listOf(weather))

        // WHEN - Activity launched and input keyword and click get data button
        launchActivity()
        Espresso.onView(withId(R.id.search_keyword_input)).perform(typeText("saigon"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.get_weather_button)).perform(click())

        // THEN - Weather info are displayed on the screen
        // make sure that the info are shown and correct
        val expectedDateText = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.date, "Wed, 16 Sep 2020")
        val expectedAverageText = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.ave_temp, 32)
        val expectedPressureText = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.pressure, 1010)
        val expectedHumidityText = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.humidity, 67)
        val expectedDescriptionText = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.description, "moderate rain")
        Espresso.onData(anything()).inAdapterView(withId(R.id.weather_list_info)).atPosition(0).onChildView(
            withId(R.id.date))
            .check(matches(ViewMatchers.withText(expectedDateText)))
        Espresso.onData(anything()).inAdapterView(withId(R.id.weather_list_info)).atPosition(0).onChildView(
            withId(R.id.average_temp))
            .check(matches(ViewMatchers.withText(expectedAverageText)))
        Espresso.onData(anything()).inAdapterView(withId(R.id.weather_list_info)).atPosition(0).onChildView(
            withId(R.id.pressure))
            .check(matches(ViewMatchers.withText(expectedPressureText)))
        Espresso.onData(anything()).inAdapterView(withId(R.id.weather_list_info)).atPosition(0).onChildView(
            withId(R.id.humidity))
            .check(matches(ViewMatchers.withText(expectedHumidityText)))
        Espresso.onData(anything()).inAdapterView(withId(R.id.weather_list_info)).atPosition(0).onChildView(
            withId(R.id.description))
            .check(matches(ViewMatchers.withText(expectedDescriptionText)))
    }

    @Test
    fun test02_searchNotAvailableKeyword_DisplayNotFound() {
        // GIVEN - Repository not available weather data for desired input keyword
        // WHEN - Activity launched and input keyword and click get data button
        launchActivity()
        Espresso.onView(withId(R.id.search_keyword_input)).perform(typeText("hanoi"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.get_weather_button)).perform(click())
        // THEN - Error view visible with message
        Espresso.onView(withId(R.id.errorText)).check(matches(isDisplayed()))
    }

    @Test
    fun test03_inputKeywordBelow3Character_GetWeatherButtonNotEnable() {
        launchActivity()
        Espresso.onView(withId(R.id.search_keyword_input)).perform(typeText("sa"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.get_weather_button)).check(matches(not(isEnabled())))
    }

    @Test
    fun test04_inputKeywordAbove3Character_GetWeatherButtonEnable() {
        launchActivity()
        Espresso.onView(withId(R.id.search_keyword_input)).perform(typeText("sai"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.get_weather_button)).check(matches(isEnabled()))
    }

    private fun launchActivity(): ActivityScenario<SearchWeatherActivity>? {
        return ActivityScenario.launch(SearchWeatherActivity::class.java)
    }
}