package com.vcco.weather.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vcco.weather.R
import com.vcco.weather.ui.state.HomeAppUiState
import com.vcco.weather.ui.state.HomeUiState
import kotlin.collections.indexOf

/**
 * Home screen view receiving the [onSearchWeatherCurrentLocationClicked] for use
 * the device location, [onSearchWeatherCLicked] when
 * search weather for city and [onLastWeatherSearchClicked] to retrieve the last search and update
 * user preference [onUnitSelectionChanged]
 */
@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    onSearchWeatherCLicked: (String) -> Unit,
    onSearchWeatherCurrentLocationClicked: () -> Unit,
    onLastWeatherSearchClicked: () -> Unit,
    onUnitSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    when (homeUiState.homeAppUiState) {
        is HomeAppUiState.NetworkError -> HomeScreenErrorView(
            errorMessage = homeUiState.homeAppUiState.error,
            unitPreference = homeUiState.units,
            onUnitSelectionChanged = onUnitSelectionChanged,
            modifier = modifier
        )

        is HomeAppUiState.FirstRun -> HomeScreenView(
            canShowLastSearchButton = false,
            unitPreference = homeUiState.units,
            onSearchWeatherCLicked = onSearchWeatherCLicked,
            onSearchWeatherCurrentLocationClicked = onSearchWeatherCurrentLocationClicked,
            onLastWeatherSearchClicked = onLastWeatherSearchClicked,
            onUnitSelectionChanged = onUnitSelectionChanged,
            modifier = modifier
        )

        is HomeAppUiState.LastWeather -> {
            if (homeUiState.isFromInit) {
                onLastWeatherSearchClicked()
            } else {
                HomeScreenView(
                    canShowLastSearchButton = true,
                    unitPreference = homeUiState.units,
                    onSearchWeatherCLicked = onSearchWeatherCLicked,
                    onSearchWeatherCurrentLocationClicked = onSearchWeatherCurrentLocationClicked,
                    onLastWeatherSearchClicked = onLastWeatherSearchClicked,
                    onUnitSelectionChanged = onUnitSelectionChanged,
                    modifier = modifier
                )
            }
        }
    }

}

/**
 * When Everything is correct, show the main UI,
 * with [canShowLastSearchButton] used to verify if we can navigate to Last Search View
 */
@Composable
fun HomeScreenView(
    canShowLastSearchButton: Boolean,
    unitPreference: String,
    onSearchWeatherCLicked: (String) -> Unit,
    onSearchWeatherCurrentLocationClicked: () -> Unit,
    onLastWeatherSearchClicked: () -> Unit,
    onUnitSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var locationSearch by remember { mutableStateOf("") }
    val context = LocalContext.current
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onSearchWeatherCurrentLocationClicked()
            } else {

                Toast.makeText(
                    context,
                    R.string.toast_location_permission_should_request,
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_8dp)),
            verticalArrangement = Arrangement.Top

        ) {
            OutlinedTextField(
                value = locationSearch,
                onValueChange = { locationSearch = it },
                placeholder = { Text(stringResource(R.string.label_search_city)) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(dimensionResource(R.dimen.padding_8dp))
                    .fillMaxWidth()
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_8dp)),
                onClick = {
                    if (locationSearch.isNotBlank()) {
                        onSearchWeatherCLicked(locationSearch)
                    } else {
                        Toast.makeText(
                            context,
                            R.string.label_search_city,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text(stringResource(R.string.button_search))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_8dp)),
                onClick = {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        onSearchWeatherCurrentLocationClicked()
                    } else {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            ) {
                Text(stringResource(R.string.button_search_current_location))
            }

            if (canShowLastSearchButton) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_8dp)),
                    onClick = {
                        onLastWeatherSearchClicked()
                    }
                ) {
                    Text(stringResource(R.string.button_show_last_search))
                }
            }
        }
        HomeScreenUnionOptionsSelector(
            unitPreference = unitPreference,
            onUnitSelectionChanged = onUnitSelectionChanged,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

/**
 * When is an Error, update ui to show the Error
 */

@Composable
fun HomeScreenErrorView(
    errorMessage: String,
    unitPreference: String,
    onUnitSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
        HomeScreenUnionOptionsSelector(
            unitPreference = unitPreference,
            onUnitSelectionChanged = onUnitSelectionChanged,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }

}

@Composable
fun HomeScreenUnionOptionsSelector(
    unitPreference: String,
    onUnitSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val unitOptions = stringArrayResource(R.array.units_option)
    val (selectedUnit, onOptionSelected) = remember {
        val index = unitOptions.indexOf(unitPreference)
        mutableStateOf(unitOptions[index])
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(R.string.label_select_unit),
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_8dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            unitOptions.forEach { item ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = (item == selectedUnit),
                            onClick = {
                                onOptionSelected(item)
                                onUnitSelectionChanged(item)
                            },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (item == selectedUnit),
                        onClick = null
                    )
                    Text(item)
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenFirstRunViewPreview() {
    HomeScreen(
        homeUiState = HomeUiState(homeAppUiState = HomeAppUiState.FirstRun),
        onSearchWeatherCLicked = {},
        onSearchWeatherCurrentLocationClicked = {},
        onUnitSelectionChanged = {},
        onLastWeatherSearchClicked = {},
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenLastSearchViewPreview() {
    HomeScreenView(
        canShowLastSearchButton = true,
        unitPreference = "Imperial",
        onSearchWeatherCLicked = {},
        onSearchWeatherCurrentLocationClicked = {},
        onUnitSelectionChanged = {},
        onLastWeatherSearchClicked = {},
        modifier = Modifier.fillMaxSize()
    )
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenErrorViewPreview() {
    HomeScreenErrorView(
        errorMessage = "No internet connection",
        unitPreference = "Imperial",
        onUnitSelectionChanged = {},
        modifier = Modifier.fillMaxSize()
    )
}
