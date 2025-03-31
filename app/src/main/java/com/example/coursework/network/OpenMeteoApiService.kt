package com.example.coursework.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.open-meteo.com/v1/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface OpenMeteoApiService {
    @GET("forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m")
    suspend fun getForecast(): String
}

object OpenMeteoApi {
    val retrofitService : OpenMeteoApiService by lazy {
        retrofit.create(OpenMeteoApiService::class.java)
    }
}