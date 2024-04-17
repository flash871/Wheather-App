package com.WheatherAppFeatures.data.retrofit.Weather

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.WheatherAppFeatures.data.retrofit.Weather.model.Forecast
import com.WheatherAppFeatures.data.retrofit.Weather.model.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException


class WeatherViewModel : ViewModel() {
    val queryText = mutableStateOf("")
    val isShowSearchLine = mutableStateOf(false)
    val weatherData = mutableStateOf(WeatherData(null, null, Forecast(forecastday = listOf())))
    val responseCity = MutableStateFlow("Санкт-Петербург")
    val selectedButtonIndex = mutableIntStateOf(1)
    val isShowAlertDialog = mutableStateOf(false)
    val todayClickCounter = mutableIntStateOf(0)
    val tenDaysClickCounter = mutableIntStateOf(0)


    fun getWeatherData() {
        viewModelScope.launch {
            try {
                val model = WeatherRetrofit.api.getForecastData(
                    "3946154e2e6142418b4104200240701",
                    responseCity.value,
                    "10",
                    "no",
                    "no"
                )
                weatherData.value = model
            } catch (e: IOException) {
                isShowAlertDialog.value = true
            }
        }
    }

    init {
        getWeatherData()
    }

}