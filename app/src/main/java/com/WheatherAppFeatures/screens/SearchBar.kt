package com.WheatherAppFeatures.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.WheatherAppFeatures.data.retrofit.Weather.WeatherViewModel
import com.WheatherAppFeatures.data.retrofit.Weather.utnils.CitiesRepository
import com.example.wheatherapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: WeatherViewModel,
    onClickBack: () -> Unit,
) {

    val currentSearchList = CitiesRepository.allCitiesList.filter {
        it.lowercase().contains(viewModel.queryText.value.lowercase())
    }

    SearchBar(
        query = viewModel.queryText.value,
        onQueryChange = { viewModel.queryText.value = it },
        onSearch = {},
        active = true,
        onActiveChange = { viewModel.isShowSearchLine.value = it },
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        trailingIcon = {
            if (viewModel.queryText.value.isNotEmpty()) {
                IconButton(onClick = { viewModel.queryText.value = "" }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "clear icon")
                }
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable { onClickBack() }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)

    ) {
        LazyColumn(
            contentPadding = PaddingValues(dimensionResource(R.dimen.small_padding))
        ) {
            items(currentSearchList) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable {
                            viewModel.responseCity.value =
                                it; viewModel.getWeatherData();onClickBack()
                        }) {
                    Text(text = it, modifier = Modifier.padding(10.dp))
                }
            }
        }
    }
}

