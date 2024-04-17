package com.WheatherAppFeatures.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.WheatherAppFeatures.data.retrofit.Weather.WeatherViewModel
import com.WheatherAppFeatures.data.retrofit.Weather.model.Hour
import com.WheatherAppFeatures.data.retrofit.Weather.utnils.getCurrentTime
import com.example.wheatherapp.R


@Composable
fun MainScreen(
    viewModel: WeatherViewModel, onSearchButtonClicked: () -> Unit,
    todayClick: () -> Unit, tenDaysClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6EDFF))


        ) {

            MainForecast(viewModel, onSearchButtonClicked)
            TwoButtons(
                todayClick = {  todayClick()  },
                tenDaysClick = {  tenDaysClick()  },
                viewModel
            )
            WeatherParameters(viewModel)
            if (viewModel.weatherData.value.forecast.forecastday.isNotEmpty()) {
                HourlyForecast(viewModel.weatherData.value.forecast.forecastday[0].hour, true)
                WeatherProgressBar(hourlyList = viewModel.weatherData.value.forecast.forecastday[0].hour)
                AstroParameters(viewModel = viewModel)
            }
            if (viewModel.isShowAlertDialog.value) {
                AlertDialog(
                    onDismissRequest = { /*TODO*/ },
                    confirmButton = {

                        TextButton(onClick = {
                            viewModel.getWeatherData(); viewModel.isShowAlertDialog.value = false
                        }) {
                            Text(text = stringResource(id = R.string.update))
                        }
                    },
                    title = {
                        Text(text = stringResource(id = R.string.error))
                    },
                    text = { Text(text = stringResource(id = R.string.IOError)) }
                )
            }
        }
    }
}

@Composable
fun MainForecast(
    viewModel: WeatherViewModel,
    onSearchButtonClicked: () -> Unit
) {

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        Image(
            painter = painterResource(id = R.drawable.main_image),
            contentDescription = "main image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomEnd = dimensionResource(
                            id = R.dimen.cornerMainImage
                        ),
                        bottomStart = dimensionResource(
                            id = R.dimen.cornerMainImage
                        )
                    )
                )
        )
        Box(modifier = Modifier.wrapContentSize(align = Alignment.Center))
        {

            MainForecastData(viewModel, onSearchButtonClicked)

        }
    }


}

@SuppressLint("InvalidColorHexValue", "StateFlowValueCalledInComposition")
@Composable
fun MainForecastData(
    viewModel: WeatherViewModel,
    onSearchButtonClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(dimensionResource(id = R.dimen.small_padding))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = viewModel.responseCity.value + ", \n${viewModel.weatherData.value.location?.country}",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.medium_padding))

            )
            IconButton(
                onClick = { onSearchButtonClicked() }, modifier = Modifier.padding(
                    top = dimensionResource(
                        id = R.dimen.iconSearch_padding
                    )
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "search icon",
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = viewModel.weatherData.value.current?.temp_c?.toInt().toString() + "°",
                color = Color.White, fontSize = 100.sp,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.small_padding))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    AsyncImageCondition(
                        url = "https://${viewModel.weatherData.value.current?.condition?.icon}",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "${viewModel.weatherData.value.current?.condition?.text}",
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = R.dimen.small_padding),
                                end = dimensionResource(id = R.dimen.small_padding)
                            )
                            .align(Alignment.End)
                    )
                }
            }
        }
        Row(modifier = Modifier.height(50.dp)) {
            Text(
                text = "feels like ${viewModel.weatherData.value.current?.feelslike_c?.toInt()}°",
                color = Color.White, modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = dimensionResource(id = R.dimen.feels_like_padding))

            )
        }
    }
}

@Composable
fun TwoButtons(todayClick: () -> Unit, tenDaysClick: () -> Unit, viewModel: WeatherViewModel) {
    val selectedButtonIndex = viewModel.selectedButtonIndex
    val pinkColor = Color(0xFFE0B6FF)

    Row {
        Row {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                onClick = { selectedButtonIndex.intValue = 1; todayClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                    if (selectedButtonIndex.intValue == 1) pinkColor else Color.White
                ),
            ) {
                Text("Today", color = Color.Black, fontSize = 12.sp)
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                onClick = { selectedButtonIndex.intValue = 3; tenDaysClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                    if (selectedButtonIndex.intValue == 3) pinkColor else Color.White
                ),

            ) {
                Text("3 days", color = Color.Black, fontSize = 12.sp)
            }
        }
    }

}

@Composable
fun AsyncImageCondition(url: String, modifier: Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun WeatherParametersCard(title: String, dataParameters: String, modifier: Modifier, icon: Int) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD0BCFF),
        )
    ) {
        Row() {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "wind speed",
                    modifier = Modifier
                        .size(25.dp)
                        .background(Color.White)
                        .padding(2.dp)

                )
            }
            Column {
                Text(text = title, modifier = Modifier.padding(5.dp))
                Text(
                    text = dataParameters,
                    modifier = Modifier.padding(
                        start = 5.dp,
                        end = 5.dp,
                        bottom = 10.dp,

                        )
                )
            }
        }
    }
}

@Composable
fun WeatherParameters(viewModel: WeatherViewModel) {
    Row {
        WeatherParametersCard(
            title = "Wind speed",
            dataParameters = "${viewModel.weatherData.value.current?.windSpeed} k/ph",
            Modifier
                .weight(1f)
                .padding(10.dp),
            icon = R.drawable.wind_speed
        )
        WeatherParametersCard(
            title = "Rain chance",
            dataParameters = if (viewModel.weatherData.value.forecast.forecastday.isNotEmpty())
                "${viewModel.weatherData.value.forecast.forecastday[0].day.daily_chance_of_rain}%"
            else "000",
            Modifier
                .weight(1f)
                .padding(10.dp),
            icon = R.drawable.rainy_icon
        )
    }
    Row {
        WeatherParametersCard(
            title = "Cloud",
            dataParameters = "${viewModel.weatherData.value.current?.cloud}%",
            Modifier
                .weight(1f)
                .padding(10.dp),
            icon = R.drawable.cloud
        )
        WeatherParametersCard(
            title = "UV Index",
            dataParameters = viewModel.weatherData.value.current?.uvIndex.toString(),
            Modifier
                .weight(1f)
                .padding(10.dp),
            icon = R.drawable.uv_index_icon
        )
    }
}


@Composable
fun HourlyForecast(hourlyList: List<Hour>, isStartCurrentTime: Boolean) {

    val currentTime = getCurrentTime()
    val newCurrentTime =
        currentTime.replaceRange(currentTime.length - 2, currentTime.length, "00")
    val startIndex = hourlyList.indexOfFirst { it.time.takeLast(5) >= newCurrentTime.takeLast(5) }
    val filteredList =
        if (isStartCurrentTime) hourlyList.subList(startIndex, hourlyList.size) else hourlyList

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD0BCFF),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(start = 5.dp, end = 5.dp, bottom = 5.dp)) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.time_left),
                        contentDescription = "hourly forecast",
                        modifier = Modifier
                            .size(25.dp)
                            .background(Color.White)
                            .padding(2.dp)

                    )
                }
                Text(
                    text = "Hourly forecast",
                    Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                items(filteredList) {
                    HourlyForecastItem(
                        temp_C = it.temp_c.toString(),
                        it.time.takeLast(5),
                        it.condition.icon
                    )
                }
            }
        }

    }
}

@Composable
fun HourlyForecastItem(temp_C: String, time: String, url: String) {
    Column {
        Text(text = time, Modifier.padding(5.dp))
        AsyncImageCondition(
            url = "https:${url}",
            modifier = Modifier
                .size(50.dp)
                .padding(5.dp)
        )
        Text(text = temp_C, Modifier.padding(5.dp))
    }
}

@Composable
fun WeatherProgressBar(hourlyList: List<Hour>) {
    val currentTime = getCurrentTime()
    val newCurrentTime =
        currentTime.replaceRange(currentTime.length - 2, currentTime.length, "00")
    val startIndex = hourlyList.indexOfFirst { it.time.takeLast(5) >= newCurrentTime.takeLast(5) }
    val filteredList = hourlyList.subList(startIndex, hourlyList.size)
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD0BCFF),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(start = 5.dp, end = 5.dp, bottom = 5.dp)) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.rainy_icon),
                        contentDescription = "chance of rain",
                        modifier = Modifier
                            .size(25.dp)
                            .background(Color.White)
                            .padding(2.dp)

                    )
                }
                Text(
                    text = "Chance of rain",
                    Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp)
                )
            }

            for (i in 0..3) {
                ProgressBarItem(
                    time = filteredList[i].time.takeLast(5),
                    progress = filteredList[i].chance_of_rain,
                    progressText = filteredList[i].chance_of_rain.toString()
                )
            }
        }

    }
}

@Composable
fun ProgressBarItem(time: String, progress: Int, progressText: String) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(text = time, fontSize = 13.sp, modifier = Modifier.padding(start = 5.dp, end = 5.dp))
        LinearProgressIndicator(
            progress = when (progress) {
                100 -> 100f
                0 -> 0f
                else -> {
                    "0.$progress".toFloat()
                }
            },
            trackColor = Color(0xFFFAEDFF),
            color = Color(0xFF8A20D5),
            modifier = Modifier
                .height(20.dp)
                .clip(RoundedCornerShape(15.dp))

        )
        Text(
            text = "$progressText%",
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )
    }
}

@Composable
fun AstroParameters(viewModel: WeatherViewModel) {
    Row {
        WeatherParametersCard(
            title = "sunrise",
            dataParameters = viewModel.weatherData.value.forecast.forecastday[0].astro.sunrise,
            Modifier
                .weight(1f)
                .padding(10.dp),
            icon = R.drawable.sunrise
        )
        WeatherParametersCard(
            title = "sunset",
            dataParameters = viewModel.weatherData.value.forecast.forecastday[0].astro.sunset,
            Modifier
                .weight(1f)
                .padding(10.dp),
            icon = R.drawable.sunset
        )
    }
}




