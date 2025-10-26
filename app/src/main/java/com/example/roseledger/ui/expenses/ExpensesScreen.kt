package com.example.roseledger.ui.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roseledger.RoseLedgerApplication
import com.example.roseledger.ui.expenses.components.ExpenseDialog
import com.example.roseledger.ui.expenses.components.ExpenseRow
import com.example.roseledger.ui.expenses.components.Header
import com.example.roseledger.ui.expenses.components.SearchBar
import com.example.roseledger.ui.viewmodel.ExpensesViewModel
import com.example.roseledger.ui.viewmodel.ExpensesViewModelFactory
import com.example.roseledger.ui.viewmodel.UiEffect
import kotlinx.coroutines.launch

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

    val isAddExpenseDialogVisible by viewModel.isAddExpenseDialogVisible.collectAsState()
    val editingExpense by viewModel.editingExpense.collectAsState()

    val categories by viewModel.categories.collectAsState()

    // LazyListState to control the scroll position of the list
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.uiEffects.collect { effect ->
            when (effect) {
                is UiEffect.ScrollToTop -> {
                    scope.launch {
                        // Animate the scroll to the top of the list
                        listState.animateScrollToItem(index = 0)
                    }
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.showAddExpenseDialog() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            val showDialog = isAddExpenseDialogVisible || editingExpense != null
            if (showDialog) {
                ExpenseDialog(
                    expenseToEdit = editingExpense, // Pass the expense to edit, or null for adding
                    categories = categories,
                    onDismissRequest = {
                        viewModel.hideAddExpenseDialog()
                        viewModel.onEditDone() // Ensure both states can close the dialog
                    },
                    onConfirm = { amount, description, category, date ->
                        if (editingExpense != null) {
                            viewModel.updateExpense(editingExpense!!.id, amount, category, description, date)
                        } else {
                            viewModel.addExpense(amount, category, description)
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            SearchBar(
                searchQuery = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) })
            Header()

            // Expense List
            LazyColumn(
                state = listState, // Attach the state to the LazyColumn
                modifier = Modifier.padding(top = 8.dp)
            ) {
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
                            onDelete = { viewModel.deleteExpense(expense.id) },
                            onEdit = { viewModel.onEditExpense(expense) },
                            displayButton = true
                        )
                    }
                }
            }

        }
    }
}