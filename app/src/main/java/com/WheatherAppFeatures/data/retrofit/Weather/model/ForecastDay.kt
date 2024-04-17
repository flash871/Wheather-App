package com.WheatherAppFeatures.data.retrofit.Weather.model

data class ForecastDay(
    val date: String,
    val uv: Float,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>,
    val condition: Conditions,
    val astro: Astro,

)
