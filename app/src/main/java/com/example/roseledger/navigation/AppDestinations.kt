package com.example.roseledger.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


/**
 * Enum class defining the navigation destinations for the app.
 *
 * @param route The unique route string for navigation.
 * @param label The text label for the destination, shown in the UI.
 * @param icon The icon representing the destination.
 * */
enum class AppDestinations(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME(
        route = "home",
        label = "Home",
        icon = Icons.Default.Home
    ),
    EXPENSES(
        route = "expenses",
        label = "Expenses",
        icon = Icons.AutoMirrored.Filled.List
    ),
    SETTINGS(
        route = "settings",
        label = "Settings",
        icon = Icons.Default.Settings
    )
}