package com.WheatherAppFeatures.data.retrofit.Weather

import com.WheatherAppFeatures.data.retrofit.Weather.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getForecastData(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: String,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String
    ): WeatherData


}