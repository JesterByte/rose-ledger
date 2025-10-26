package com.example.roseledger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roseledger.data.model.Expense
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
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.util.Calendar

/**
 * Represents the state of the Reports screen.
 *
 * @param totalExpenses The total monetary value of all expenses.
 * @param expensesByCategory A map where keys are category names and values are the sum of expenses for that category.
 */
data class ReportsUiState(
    val totalExpenses: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val currentFilter: DateFilter = DateFilter.MONTH,
    val dateRangeLabel: String = "",
    val chartData: List<ChartEntry> = emptyList() // Add data for the chart
)

/**
 * ViewModel for the Reports screen.
 * It processes expense data to create a summary for the UI.
 *
 * @param repository The repository for accessing expense data.
 */
class ReportsViewModel(repository: ExpenseRepository) : ViewModel() {

    private val _currentFilter = MutableStateFlow(DateFilter.MONTH) // The current filter state.

    // Combine the filter state with the database flow
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ReportsUiState> = _currentFilter
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

                val chartData = generateChartData(expenses, filter)

                ReportsUiState(
                    totalExpenses = total,
                    expensesByCategory = byCategory,
                    currentFilter = filter,
                    dateRangeLabel = DateUtils.getFormattedDateRange(filter),
                    chartData = chartData
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ReportsUiState() // Initial empty state
        )

    private fun generateChartData(
        expenses: List<Expense>,
        filter: DateFilter
    ): List<ChartEntry> {
        if (expenses.isEmpty()) return emptyList()

        val calendar = Calendar.getInstance()

        val groupedExpenses = when (filter) {
            DateFilter.DAY -> {
                // Group by hour of the day (0-23)
                expenses.groupBy {
                    calendar.time = it.date
                    calendar.get(Calendar.HOUR_OF_DAY)
                }
            }
            DateFilter.WEEK -> {
                // Group by day of the week (1=Sun, 2-Mon, ...)
                expenses.groupBy {
                    calendar.time = it.date
                    calendar.get(Calendar.DAY_OF_WEEK)
                }
            }
            DateFilter.MONTH -> {
                // Group by day of the month
                expenses.groupBy {
                    calendar.time = it.date
                    calendar.get(Calendar.DAY_OF_MONTH)
                }
            }
            DateFilter.YEAR -> {
                // Group by month of the year (0=Jan, 1=Feb, ...)
                expenses.groupBy {
                    calendar.time = it.date
                    calendar.get(Calendar.MONTH)
                }
            }
            // Expandable for DAY or ALL filter
            else -> return emptyList()
        }

        // Map the grouped data to ChartEntry objects
        return groupedExpenses.map { (key, expenseList) ->
            FloatEntry(key.toFloat(), expenseList.sumOf { it.amount }.toFloat())
        }.sortedBy { it.x } // Sort by the x-value (day, month, etc.)
    }

    fun setDateFilter(filter: DateFilter) {
        _currentFilter.value = filter
    }
}

/**
 * Factory for creating an instance of ReportsViewModel with a repository dependency.
 */
class ReportsViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}