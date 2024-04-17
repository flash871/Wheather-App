package com.WheatherAppFeatures.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.WheatherAppFeatures.data.retrofit.Weather.WeatherViewModel
import com.example.wheatherapp.ui.theme.WheatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WheatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    WeatherApp(WeatherViewModel())
                }
            }
        }
    }
}

enum class WeatherScreens {
    Main, Search, TenDaysForecast
}

@Composable
fun WeatherApp(viewModel: WeatherViewModel) {
    var todayClickCounter = viewModel.todayClickCounter.intValue
    var tenDaysCounter = viewModel.tenDaysClickCounter.intValue
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.Main.name) {
        composable(route = WeatherScreens.Main.name) {
            MainScreen(
                viewModel = viewModel,
                todayClick = {
                    tenDaysCounter = 0; todayClickCounter++; if (todayClickCounter > 1) {
                } else navController.popBackStack( WeatherScreens.Main.name, inclusive = false);
                },
                tenDaysClick = {
                    todayClickCounter = 0; tenDaysCounter++; if (tenDaysCounter > 1) {
                } else navController.navigate(route = WeatherScreens.TenDaysForecast.name);
                },
                onSearchButtonClicked = { navController.navigate(route = WeatherScreens.Search.name) }
            )
        }
        composable(route = WeatherScreens.Search.name) {
            SearchScreen(
                viewModel,
                onClickBack = {
                    navController.popBackStack(
                        WeatherScreens.Main.name,
                        inclusive = false
                    )
                })

        }
        composable(route = WeatherScreens.TenDaysForecast.name) {
            TenDaysScreen(
                viewModel = viewModel,
                todayClick = {
                   tenDaysCounter= 0; todayClickCounter++;if (todayClickCounter > 1) {
                    } else navController.popBackStack( WeatherScreens.Main.name, inclusive = false);
                },
                tenDaysClick = {
                   tenDaysCounter++; todayClickCounter =0;if (tenDaysCounter > 1) {
                } else navController.navigate(route = WeatherScreens.TenDaysForecast.name);
                },
            )
        }
    }
}