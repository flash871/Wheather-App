package com.WheatherAppFeatures.data.retrofit.Weather.model

import com.google.gson.annotations.SerializedName

data class Current(
    val feelslike_c: Float?,
    val temp_c: Float?,
    val condition: Conditions,
    @SerializedName("wind_kph")
    val windSpeed: Float?,
    @SerializedName("uv")
    val uvIndex: Float?,
    val pressure_in:Float?,
    val cloud: Int

)
