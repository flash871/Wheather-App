package com.WheatherAppFeatures.data.retrofit.Weather.model

data class Day(
    val maxtemp_c: Float,
    val daily_chance_of_rain: Int,
    val condition: Conditions,
    val avgtemp_c: Float
)
