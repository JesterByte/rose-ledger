package com.example.roseledger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roseledger.data.model.Expense
import com.example.roseledger.data.repository.ExpenseRepository
import com.example.roseledger.util.DateFilter
import com.example.roseledger.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

/**
 * Represents the state of the Home screen.
 *
 * @property monthlyTotal The total amount spent in the current month.
 * @property recentExpenses The most recent 5 expenses.
 * @property topCategory The category with the highest total amount spent.
 * @property averageDailySpend The average amount spent per day.
 * @property transactionCount The total number of transactions.
 */
data class HomeUiState(
    val monthlyTotal: Double = 0.0,
    val recentExpenses: List<Expense> = emptyList(),
    val topCategory: Pair<String, Double>? = null,
    val averageDailySpend: Double = 0.0,
    val transactionCount: Int = 0
)

/**
 * ViewModel for the Home screen.
 * It processes expense data to create a summary for the UI.
 *
 * @param repository The repository for accessing expense data.
 */
class HomeViewModel(repository: ExpenseRepository) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = repository
        .getExpensesBetween(
            DateUtils.getDateRange(DateFilter.MONTH).first,
            DateUtils.getDateRange(DateFilter.MONTH).second
        )
        .map { monthlyExpenses ->
            val monthlyTotal = monthlyExpenses.sumOf { it.amount }

            val topCategory = monthlyExpenses
                .groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
                .maxByOrNull { it.value }
                ?.toPair()

            val calendar = Calendar.getInstance()
            val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val averageDailySpend = monthlyTotal / currentDayOfMonth
            val transactionCount = monthlyExpenses.size

            HomeUiState(
                monthlyTotal = monthlyExpenses.sumOf { it.amount },
                recentExpenses = monthlyExpenses.take(5),
                topCategory = topCategory,
                averageDailySpend = averageDailySpend,
                transactionCount = transactionCount
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUiState() // Initial empty state
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