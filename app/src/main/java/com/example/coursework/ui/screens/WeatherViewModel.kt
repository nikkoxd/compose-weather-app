package com.example.coursework.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.network.WeatherApi
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    val condition = mutableStateOf("")

    init {
        getWeather()
    }

    fun getWeather() {
        viewModelScope.launch {
            try {
                val response = WeatherApi.retrofitService.getCurrent()

                condition.value = response.current.condition.text
            } catch (e: Exception) {
                condition.value = "Error: ${e.message}"
            }
        }
    }
}