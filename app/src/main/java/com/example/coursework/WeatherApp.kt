package com.example.coursework

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coursework.ui.screens.WeatherViewModel

@Composable
fun WeatherApp(modifier: Modifier) {
    val viewModel: WeatherViewModel = viewModel()
    val condition = viewModel.condition.value

    Column(
        modifier = modifier
    ) {
        Text(condition)
        Button(onClick = { viewModel.getWeather() }) {
            Text("Refresh weather")
        }
    }
}