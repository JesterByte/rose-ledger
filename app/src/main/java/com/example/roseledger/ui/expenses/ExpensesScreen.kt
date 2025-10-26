package com.example.roseledger.ui.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roseledger.RoseLedgerApplication
import com.example.roseledger.ui.expenses.components.AddExpenseDialog
import com.example.roseledger.ui.expenses.components.ExpenseRow
import com.example.roseledger.ui.expenses.components.Header
import com.example.roseledger.ui.expenses.components.SearchBar
import com.example.roseledger.ui.viewmodel.ExpensesViewModel
import com.example.roseledger.ui.viewmodel.ExpensesViewModelFactory

@Composable
fun ExpensesScreen() {
    // Get the application context to access the repository.
    val context = LocalContext.current.applicationContext
    val application = context as RoseLedgerApplication
    // Create the viewModel using the factory, providing the repository.
    val viewModel: ExpensesViewModel = viewModel(
        factory = ExpensesViewModelFactory(
            expenseRepository = application.expenseRepository,
            categoryRepository = application.categoryRepository
        )
    )

    val expenses by viewModel.expenses.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isDialogVisible by viewModel.isAddExpenseDialogVisible.collectAsState()

    val categories by viewModel.categories.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.showAddExpenseDialog() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            // Show the dialog when the state is true
            if (isDialogVisible) {
                AddExpenseDialog(
                    categories = categories,
                    onDismissRequest = { viewModel.hideAddExpenseDialog() },
                    onConfirm = { amount, description, category ->
                        viewModel.addExpense(amount, category, description)
                        viewModel.hideAddExpenseDialog()
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            SearchBar(searchQuery = searchQuery, onValueChange = { viewModel.onSearchQueryChange(it) })
            Header()

            // Expense List
            LazyColumn {
                if (expenses.isEmpty()) {
                    item {
                        Text(
                            text = if (searchQuery.isNotBlank()) "No results found." else "No expenses yet. Add one!",
                            modifier = Modifier.padding(vertical = 16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(expenses, key = { it.id }) { expense ->
                        ExpenseRow(
                            expense = expense,
                            onDelete = { viewModel.deleteExpense(expense.id) }
                        )
                    }
                }
            }

        }
    }
}