package com.vcco.weather.ui.navigation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import com.vcco.weather.R
import com.vcco.weather.navigation.WeatherScreen
import com.vcco.weather.ui.screens.DetailScreen
import com.vcco.weather.ui.screens.HistoryScreen
import com.vcco.weather.ui.screens.HomeScreen
import com.vcco.weather.ui.screens.LastSearchDetailScreen
import com.vcco.weather.ui.state.HomeAppUiState
import com.vcco.weather.ui.toolbar.WeatherAppToolBar
import com.vcco.weather.ui.viewmodel.MainActivityViewModel

/**
 * Navigation Graph for Weather App
 *
 */
@Composable
fun WeatherApp(
    vm: MainActivityViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current

    val homeAppUiState by vm.homeScreenUiState.collectAsState()

    val detailAppUiState by vm.detailScreenUiState.collectAsState()

    val historyUiState by vm.historyScreenUiState.collectAsState()

    val blackStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        WeatherScreen.valueOf(
            blackStackEntry?.destination?.route ?: WeatherScreen.Home.name,
        )

    Scaffold(topBar = {
        WeatherAppToolBar(
            currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = {
                navController.navigateUp()
            },
        )
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WeatherScreen.Home.name,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            composable(route = WeatherScreen.Home.name) {
                HomeScreen(
                    homeUiState = homeAppUiState,
                    onSearchWeatherCLicked = {
                        searchWeather(location = it, vm = vm, context = context)
                        navController.navigate(WeatherScreen.Detail.name)
                    },
                    onSearchWeatherCurrentLocationClicked = {
                        getLastKnowLocation(
                            context = context,
                            vm = vm,
                            navController = navController,
                        )
                    },
                    onLastWeatherSearchClicked = {
                        vm.updateIsFromInitHomeUIState()
                        navController.navigate(WeatherScreen.LastSearch.name)
                    },
                    onUnitSelectionChanged = {
                        vm.updateUnitPreference(it)
                    },
                    onAllSearchClicked = {
                        navController.navigate(WeatherScreen.HistorySearch.name)
                        vm.getAllWeatherSearch(context)
                    },
                    onLastFiveSearchClicked = {
                        navController.navigate(WeatherScreen.HistorySearch.name)
                        vm.getMostFiveRecentWeatherSearch(context)
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_16dp)),
                )
            }
            composable(route = WeatherScreen.Detail.name) {
                DetailScreen(
                    detailAppUiState = detailAppUiState,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_16dp)),
                )
            }
            composable(route = WeatherScreen.LastSearch.name) {
                LastSearchDetailScreen(
                    lastWeatherResponse = (homeAppUiState.homeAppUiState as HomeAppUiState.LastWeather).lastWeatherResponse,
                    onBackClicked = {
                        navController.navigateUp()
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_16dp)),
                )
            }
            composable(route = WeatherScreen.HistorySearch.name) {
                HistoryScreen(
                    historyAppUiState = historyUiState,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_16dp)),
                )
            }
        }
    }
}

/**
 * search weather from requested [location] could be zip code or place name
 */
private fun searchWeather(
    location: String,
    vm: MainActivityViewModel,
    context: Context,
) {
    if (location.matches(".*\\d+.*".toRegex())) {
        // remove spaces, causing the search fail when is a valid zip code
        vm.getInfoFromZipCode(location.filter { !it.isWhitespace() }, context)
    } else {
        vm.searchCityWeather(location, false, context)
    }
}

/**
 * Function used to get the last know location of the device, to retrieve location
 * and make network to search location
 */

private fun getLastKnowLocation(
    context: Context,
    vm: MainActivityViewModel,
    navController: NavHostController,
) {
    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED
    ) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    vm.searchWeatherFromLocation(
                        it.latitude.toFloat(),
                        it.longitude.toFloat(),
                        context,
                    )
                    navController.navigate(WeatherScreen.Detail.name)
                }
            }
        } else {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
    }
}
