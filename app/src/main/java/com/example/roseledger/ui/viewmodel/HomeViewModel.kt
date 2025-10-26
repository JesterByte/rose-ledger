package com.example.roseledger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roseledger.data.repository.ExpenseRepository
import com.example.roseledger.util.DateFilter
import com.example.roseledger.util.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
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
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val currentFilter: DateFilter = DateFilter.MONTH,
    val dateRangeLabel: String = ""
)

/**
 * ViewModel for the Home (Dashboard) screen.
 * It processes expense data to create a summary for the UI.
 *
 * @param repository The repository for accessing expense data.
 */
class HomeViewModel(repository: ExpenseRepository) : ViewModel() {

    private val _currentFilter = MutableStateFlow(DateFilter.MONTH) // The current filter state.

    // Combine the filter state with the database flow
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = _currentFilter
        .flatMapLatest { filter ->
            val (startDate, endDate) = DateUtils.getDateRange(filter)
            // Use the appropriate repository function based on the filter
            val expensesFlow = if (filter == DateFilter.ALL) {
                repository.allExpenses
            } else {
                repository.getExpensesBetween(startDate, endDate)
            }

            // Map the results to our UI state
            expensesFlow.map { expenses ->
                val total = expenses.sumOf { it.amount }
                val byCategory = expenses.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                HomeUiState(
                    totalExpenses = total,
                    expensesByCategory = byCategory,
                    currentFilter = filter,
                    dateRangeLabel = DateUtils.getFormattedDateRange(filter)
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUiState() // Initial empty state
        )

    fun setDateFilter(filter: DateFilter) {
        _currentFilter.value = filter
    }
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