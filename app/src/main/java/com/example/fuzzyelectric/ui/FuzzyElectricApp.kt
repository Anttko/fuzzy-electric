package com.example.fuzzyelectric.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.fuzzyelectric.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fuzzyelectric.ui.screens.FuzzyElectricViewModel
import com.example.fuzzyelectric.ui.screens.HomeScreen



enum class ElectricScreen(@StringRes val title: Int) {
    HomeScreen(title = R.string.app_name),
    WeekView(title=R.string.week_view),
}

@Composable
fun ElectricAppBar(
    currentScreen: ElectricScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.return_button)
                    )
                }
            }
        }
    )
}
@Composable
fun FuzzyElectricApp(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {


    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        ElectricScreen.valueOf(backStackEntry?.destination?.route ?: ElectricScreen.HomeScreen.name)

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            ElectricAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        NavHost(navController = navController, startDestination = ElectricScreen.HomeScreen.name, modifier = modifier.padding(innerPadding)) {

            composable(route = ElectricScreen.HomeScreen.name) {
                Surface(
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    color = MaterialTheme.colors.background
                ) {
                    val fuzzyElectricViewModel: FuzzyElectricViewModel =
                        viewModel(factory = FuzzyElectricViewModel.Factory)
                    HomeScreen(
                        fuzzyElectricUiState = fuzzyElectricViewModel.fuzzyElectricUiState,
                        retryAction = fuzzyElectricViewModel::getPrices,
                        navigateButton = {
                            navController.navigate(ElectricScreen.WeekView.name)
                        }
                    )

                }
            }
        }
    }
}



        /*
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            val fuzzyElectricViewModel: FuzzyElectricViewModel =
                viewModel(factory = FuzzyElectricViewModel.Factory)
            HomeScreen(
                fuzzyElectricUiState = fuzzyElectricViewModel.fuzzyElectricUiState,
                retryAction = fuzzyElectricViewModel::getPrices
            )
        }*/
    //}
//}
