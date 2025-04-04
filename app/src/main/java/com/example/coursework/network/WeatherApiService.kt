package com.example.coursework.network

import com.example.coursework.ForecastResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "06008a534fe7418c84d123839240108"
private const val BASE_URL = "http://api.weatherapi.com/v1/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface WeatherApiService {
    @GET("forecast.json?key=$API_KEY&days=3&&aqi=no&alerts=no")
    suspend fun getForecast(@Query("q") q: String): ForecastResponse
}

object WeatherApi {
    val retrofitService : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}