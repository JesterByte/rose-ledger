package com.example.roseledger.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.roseledger.ui.expenses.ExpensesScreen
import com.example.roseledger.ui.home.HomeScreen
import com.example.roseledger.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME.route,
        modifier = modifier
    ) {
        composable(AppDestinations.HOME.route) {
            HomeScreen()
        }
        composable(AppDestinations.EXPENSES.route) {
            ExpensesScreen()
        }
        composable(AppDestinations.SETTINGS.route) {
            SettingsScreen()
        }
    }
}