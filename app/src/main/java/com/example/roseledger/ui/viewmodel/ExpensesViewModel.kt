package com.example.roseledger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roseledger.data.model.Expense
import com.example.roseledger.data.repository.CategoryRepository
import com.example.roseledger.data.repository.ExpenseRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel for the Expenses screen.
 * It holds the UI state and communicates with the repository.
 *
 * @param repository The repository for accessing expense data.
 */
class ExpensesViewModel(
    private val repository: ExpenseRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    // Add a Channel for side affects
    private val _uiEffects = Channel<UiEffect>()
    val uiEffects = _uiEffects.receiveAsFlow()

    // State to hold the currently editing expense. Null means no expense is being edited.
    private val _editingExpense = MutableStateFlow<Expense?>(null)
    val editingExpense = _editingExpense.asStateFlow()

    // A private MutableStateFlow to hold the search query from the user.
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // State to control the visibility of the "Add Expense" dialog.
    private val _isAddExpenseDialogVisible = MutableStateFlow(false)
    val isAddExpenseDialogVisible = _isAddExpenseDialogVisible.asStateFlow()

    // A private MutableStateFlow for demonstration purposes, to add new expenses.
//    private val _newExpense = MutableStateFlow<Expense?>(null)

    // This combines the stream of all expenses with the current search query.
    // The UI will update automatically whenever either of them changes.
    val expenses = repository.allExpenses.combine(_searchQuery) { expenses, query ->
        if (query.isBlank()) {
            expenses
        } else {
            expenses.filter {
                it.description.contains(query, ignoreCase = true) || it.category.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
    }.stateIn(
        // The state flow is managed within the viewModelScope.
        scope = viewModelScope,
        // The flow starts when a collector is present and stops 5 seconds after the last collector leaves.
        started = SharingStarted.WhileSubscribed(5000L),
        // The initial value of the StateFlow.
        initialValue = emptyList()
    )

    /**
     * Updates the search query.
     */
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onEditExpense(expense: Expense) {
        _editingExpense.value = expense
    }

    fun onEditDone() {
        _editingExpense.value = null
    }

    fun onAddDone() {
        _isAddExpenseDialogVisible.value = false
    }

    fun updateExpense(id: Int, amount: Double, category: String, description: String, date: Date) {
        viewModelScope.launch {
            repository.update(
                Expense(
                    id = id,
                    amount = amount,
                    category = category,
                    description = description,
                    date = date
                )
            )
            // Send scroll-to-top effect
            _uiEffects.send(UiEffect.ScrollToTop)
        }
        onEditDone() // Close the dialog after updating
    }

    /**
     * Inserts a new expense into the database.
     */
    fun addExpense(amount: Double, category: String, description: String) {
        viewModelScope.launch {
            repository.insert(
                Expense(
                    amount = amount,
                    category = category,
                    description = description,
                    date = Date() // Uses the current date
                )
            )
            _uiEffects.send(UiEffect.ScrollToTop)
        }
        onAddDone()
    }

    /**
     * Deletes an expense from the database.
     */
    fun deleteExpense(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    /**
     * Shows the "Add Expense" dialog.
     */
    fun showAddExpenseDialog() {
        _isAddExpenseDialogVisible.value = true
    }

    /**
     * Hides the "Add Expense" dialog.
     */
    fun hideAddExpenseDialog() {
        _isAddExpenseDialogVisible.value = false
    }

    val categories = categoryRepository.allCategories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}

// Sealed class to define possible UI side effects
sealed class UiEffect {
    data object ScrollToTop : UiEffect()
}

/**
 * Factory for creating an instance of ExpensesViewModel with a repository dependency.
 */
class ExpensesViewModelFactory(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(expenseRepository, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}