package com.example.roseledger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roseledger.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Represents the state of the Home screen.
 *
 * @param totalExpenses The total monetary value of all expenses.
 * @param expensesByCategory A map where keys are category names and values are the sum of expenses for that category.
 */
data class HomeUiState(
    val totalExpenses: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap()
)

/**
 * ViewModel for the Home (Dashboard) screen.
 * It processes expense data to create a summary for the UI.
 *
 * @param repository The repository for accessing expense data.
 */
class HomeViewModel(repository: ExpenseRepository) : ViewModel() {

    // This StateFlow will hold the calculated summary data.
    val uiState: StateFlow<HomeUiState> = repository.allExpenses
        .map { expenses ->
            // Calculate the total expenses by summing up the amounts
            val total = expenses.sumOf { it.amount }

            // Group expenses by category and sum their amounts
            val byCategory = expenses.groupBy { it.category }
                .mapValues { entry ->  entry.value.sumOf { it.amount } }

            // Create the UI stage object
            HomeUiState(totalExpenses = total, expensesByCategory = byCategory)
        }
        .stateIn(
            scope = viewModelScope,
            // The flow starts when a collector is present and stops 5 seconds after the last collector disappears.
            started = SharingStarted.WhileSubscribed(5000L),
            // The initial state while the data is loading.
            initialValue = HomeUiState()
        )
}

/**
 * Factory for creating an instance of HomeViewModel with a repository dependency.
 */
class HomeViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}