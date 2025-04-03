package com.example.coursework

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coursework.ui.screens.WeatherViewModel

@Composable
fun WeatherApp(modifier: Modifier) {
    val viewModel: WeatherViewModel = viewModel()
    val weather = viewModel.weather.value
    val error = viewModel.error.value

    if (error != "") {
        Text(
            text = error,
            modifier = modifier
        )
    } else {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = modifier.padding(horizontal = 12.dp).fillMaxSize()
        ) {
            CityInfo(
                weather = weather,
                modifier = Modifier.fillMaxWidth()
            )
            Icon(
                painter = painterResource(
                    weatherIconMap.getOrDefault(
                        weather.current.condition.code,
                        R.drawable.sunny_24px
                    )
                ),
                contentDescription = weather.current.condition.text,
                modifier = Modifier.size(64.dp).align(Alignment.CenterHorizontally)
            )
            CurrentInfo(
                weather = weather,
                modifier = Modifier.height(72.dp).fillMaxWidth()
            )
            TodayInfo(
                weather = weather,
                modifier = Modifier.fillMaxWidth()
            )
            ThreeDaysInfo(
                weather = weather,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CityInfo(weather: ForecastResponse, modifier: Modifier) {
    Column (modifier = modifier) {
        Text(
            text = weather.location.name,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Text(
            text = weather.current.condition.text,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun CurrentInfo(weather: ForecastResponse, modifier: Modifier) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column (
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxHeight().weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.air_24px),
                contentDescription = "Wind speed",
                modifier = Modifier.size(32.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = "${weather.current.wind_kph} km/h",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Column (
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxHeight().weight(2f)
        ) {
            Text(
                text = "${weather.current.temp_c} °C",
                fontSize = 36.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Feels like ${weather.current.feelslike_c} °C",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Column (
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxHeight().weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.humidity_percentage_24px),
                contentDescription = "Humidity",
                modifier = Modifier.size(32.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = "${weather.current.humidity}%",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun TodayInfo(weather: ForecastResponse, modifier: Modifier) {
    if (weather.forecast.forecastday.isNotEmpty()) {
        Card(modifier = modifier) {
            LazyRow (modifier = Modifier.padding(8.dp)) {
                items(24) { hour ->
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = (hour).toString(),
                        )
                        Icon(
                            painter = painterResource(
                                weatherIconMap.getOrDefault(
                                    weather.forecast.forecastday[0].hour[hour].condition.code,
                                    R.drawable.sunny_24px
                                )
                            ),
                            contentDescription = weather.current.condition.text,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = "${weather.forecast.forecastday[0].hour[hour].temp_c}°",
                        )
                    }
                }
            }
        }
    }
}