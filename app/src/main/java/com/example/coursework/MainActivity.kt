package com.example.coursework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coursework.ui.screens.WeatherViewModel
import com.example.coursework.ui.theme.CourseworkTheme
import java.time.Instant
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: WeatherViewModel = viewModel()
            val cities = listOf(
                "Izhevsk",
                "Moscow",
                "London",
            )

            CourseworkTheme {
                Scaffold(
                    topBar = {
                        CitySearchBar(viewModel, cities, modifier = Modifier.fillMaxWidth())
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    WeatherApp(viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WeatherApp(viewModel: WeatherViewModel, modifier: Modifier) {
    val weather = viewModel.weather.value
    val error = viewModel.error.value

    if (error != "") {
        Text(
            text = error,
            modifier = modifier
        )
    } else {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(top = 16.dp).padding(horizontal = 12.dp).fillMaxSize()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchBar(
    viewModel: WeatherViewModel,
    cities: List<String>,
    modifier: Modifier
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Box (modifier = modifier) {
        SearchBar(
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                active = false
                if (it != "") {
                    viewModel.getWeather(it)
                }
            },
            active = active,
            onActiveChange = {
                active = it
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.search_24px),
                    contentDescription = "Search"
                )
            },
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            Column (Modifier.verticalScroll(rememberScrollState())) {
                cities.forEach { city ->
                    ListItem(
                        headlineContent = { Text(city) },
                        modifier = Modifier
                            .clickable {
                                text = city
                                active = false
                                viewModel.getWeather(city)
                            }
                            .fillMaxWidth()
                    )
                }
            }
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
                text = "${weather.current.temp_c}°",
                fontSize = 36.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Feels like ${weather.current.feelslike_c}°",
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
        Column(modifier = modifier) {
            Text(
                text = "Hourly forecast",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card {
                LazyRow(modifier = Modifier.padding(8.dp)) {
                    items(24) { hour ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Text(
                                text = getHourString(hour)
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
}

fun getHourString(hour: Int): String {
    return if (hour - 12 > 0) {
        "${hour - 12} PM"
    } else {
        "${if (hour == 0) 12 else hour} ${if (hour == 12) "PM" else "AM"}"
    }
}

@Composable
fun ThreeDaysInfo(weather: ForecastResponse, modifier: Modifier) {
    if (weather.forecast.forecastday.isNotEmpty()) {
        val calendar = Calendar.getInstance()
        Column (modifier = modifier) {
            Text(
                text = "3-days forecast",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn {
                items (weather.forecast.forecastday.size) { day ->
                    Card (modifier = Modifier.padding(vertical = 4.dp)) {
                        val timestamp =
                            Instant.ofEpochSecond(weather.forecast.forecastday[day].date_epoch)
                        val date = Date.from(timestamp)
                        calendar.time = date
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(12.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = getDayOfWeekName(calendar.get(Calendar.DAY_OF_WEEK)),
                                modifier = Modifier.weight(2f)
                            )
                            Icon(
                                painter = painterResource(
                                    weatherIconMap.getOrDefault(
                                        weather.forecast.forecastday[day].day.condition.code,
                                        R.drawable.sunny_24px
                                    )
                                ),
                                contentDescription = weather.current.condition.text,
                                modifier = Modifier.weight(1f)
                            )
                            Row (modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${weather.forecast.forecastday[day].day.maxtemp_c}°",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "/${weather.forecast.forecastday[day].day.mintemp_c}°",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getDayOfWeekName(dayOfWeek: Int) : String {
    return when (dayOfWeek) {
        1 -> "Sunday"
        2 -> "Monday"
        3 -> "Tuesday"
        4 -> "Wednesday"
        5 -> "Thursday"
        6 -> "Friday"
        7 -> "Saturday"
        else -> "Monday"
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CourseworkTheme {
//        Greeting("Android")
//    }
//}