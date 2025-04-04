package com.example.coursework.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.ForecastResponse
import com.example.coursework.network.WeatherApi
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    val weather = mutableStateOf(ForecastResponse())
    val error = mutableStateOf("")

    init {
        getWeather("Izhevsk")
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = WeatherApi.retrofitService.getForecast(city)

                weather.value = response
            } catch (e: Exception) {
                error.value = "Error: ${e.message}"
            }
        }
    }
}