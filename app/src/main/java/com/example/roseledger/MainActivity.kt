package com.example.roseledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.roseledger.data.preferences.ThemeSetting
import com.example.roseledger.navigation.AppDestinations
import com.example.roseledger.navigation.AppNavHost
import com.example.roseledger.ui.theme.RoseLedgerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themePreferences = (application as RoseLedgerApplication).themePreferences
            val themeSetting by themePreferences.themeSetting.collectAsState(initial = ThemeSetting.SYSTEM)

            val useDarkTheme = when (themeSetting) {
                ThemeSetting.LIGHT -> false
                ThemeSetting.DARK -> true
                ThemeSetting.SYSTEM -> isSystemInDarkTheme()
            }

            RoseLedgerTheme(darkTheme = useDarkTheme) {
                RoseLedgerApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun RoseLedgerApp() {
    // The NavController is the single source of truth for navigation
    val navController = rememberNavController()
    // Get the current route to determine which nav item is selected
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            // Iterate through defined destinations
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    // The item is selected if its route matches the current route
                    selected = currentRoute == destination.route,
                    onClick = {
                        // Navigate to the destination's route
                        navController.navigate(destination.route) {
                            // Pop up to the start destination of the graph to avoid building up a large back stack as users select items.
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoseLedgerPreview() {
    RoseLedgerTheme {
        RoseLedgerApp()
    }
}