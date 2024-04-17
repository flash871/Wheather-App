package com.WheatherAppFeatures.screens

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.WheatherAppFeatures.data.retrofit.Weather.WeatherViewModel
import com.WheatherAppFeatures.data.retrofit.Weather.model.Hour

@Composable
fun TenDaysScreen(viewModel: WeatherViewModel, todayClick: () -> Unit, tenDaysClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF6EDFF))
    ) {
        TwoButtons(todayClick = { todayClick() }, tenDaysClick = { tenDaysClick() }, viewModel)
        if (viewModel.weatherData.value.isNotNull()) {
            ForecastList(viewModel = viewModel)
        }
    }
    val context = LocalContext.current

    DisposableEffect(key1 = context) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                run { todayClick(); viewModel.selectedButtonIndex.intValue = 1 }
            }
        }

        (context as ComponentActivity).onBackPressedDispatcher.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }

}

@Composable
fun ForecastList(viewModel: WeatherViewModel) {

    for (i in 0..2) {
        DayForecastItem(
            date = viewModel.weatherData.value.forecast.forecastday[i].date,
            temperature = viewModel.weatherData.value.forecast.forecastday[i].day.avgtemp_c.toInt()
                .toString(),
            weatherText = viewModel.weatherData.value.forecast.forecastday[i].day.condition.text,
            hourlyList = viewModel.weatherData.value.forecast.forecastday[i].hour,
            weatherIconUrl = viewModel.weatherData.value.forecast.forecastday[i].day.condition.icon
        )
    }
}

@Composable
fun DayForecastItem(
    date: String,
    temperature: String,
    weatherText: String,
    hourlyList: List<Hour>,
    weatherIconUrl: String
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEBDEFF)
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
        ) {


            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(text = date, Modifier.padding(5.dp), color = Color.Black)
                    Text(text = weatherText, Modifier.padding(5.dp), color = Color.Gray)

                }
                Spacer(modifier = Modifier.weight(1f))
                Row(Modifier.padding(top = 10.dp)) {
                    Text(
                        text = "$temperature°",
                        modifier = Modifier.padding(5.dp),
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    AsyncImageCondition(
                        url = "https:$weatherIconUrl",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                    )
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                }
            }
            if (expanded) {
                HourlyForecast(hourlyList = hourlyList, false)
            }
        }

    }
}