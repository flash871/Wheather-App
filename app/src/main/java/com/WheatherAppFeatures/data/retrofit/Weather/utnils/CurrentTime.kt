package com.WheatherAppFeatures.data.retrofit.Weather.utnils

import com.WheatherAppFeatures.data.retrofit.Weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentTime(): String {
    val pattern = "yyyy-MM-dd HH:mm"
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    val currentTime = Date()
    return sdf.format(currentTime)
}


