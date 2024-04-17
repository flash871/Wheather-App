package com.WheatherAppFeatures.data.retrofit.Weather.model

data class Hour(
    var time: String,
    val temp_c: Float,
    val condition: Conditions,
    val chance_of_rain:Int
)
