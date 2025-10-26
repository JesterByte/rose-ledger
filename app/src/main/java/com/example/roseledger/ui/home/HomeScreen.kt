package com.example.roseledger.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roseledger.RoseLedgerApplication
import com.example.roseledger.ui.home.components.HomeScreenContent
import com.example.roseledger.ui.viewmodel.HomeViewModel
import com.example.roseledger.ui.viewmodel.HomeViewModelFactory

@Composable
fun HomeScreen() {
    // Get the application context to access the repository.
    val context = LocalContext.current.applicationContext
    val application = context as RoseLedgerApplication
    // Create the viewModel using the factory, providing the repository.
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            repository = application.expenseRepository
        )
    )

    // Collect the UI state from the ViewModel. The UI will recompose when this state changes.
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(uiState = uiState, onFilterChange = { viewModel.setDateFilter(it) } )
}



