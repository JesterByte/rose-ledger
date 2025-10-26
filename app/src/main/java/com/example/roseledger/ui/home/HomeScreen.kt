package com.example.roseledger.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roseledger.RoseLedgerApplication
import com.example.roseledger.ui.expenses.components.ExpenseRow
import com.example.roseledger.ui.home.components.InsightCard
import com.example.roseledger.ui.viewmodel.HomeViewModel
import com.example.roseledger.ui.viewmodel.HomeViewModelFactory
import com.example.roseledger.util.formatAmountWithCommas

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

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("This Month's Spending", style = MaterialTheme.typography.titleMedium)
                Text(
                    formatAmountWithCommas(uiState.monthlyTotal),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${uiState.transactionCount} Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            InsightCard(
                title = "Top Category",
                value = uiState.topCategory?.first ?: "N/A",
                modifier = Modifier.weight(1f)
            )
            InsightCard(
                title = "Avg. Daily Spend",
                value = formatAmountWithCommas(uiState.averageDailySpend),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(8.dp))

        Text("Recent Activity", style = MaterialTheme.typography.titleLarge)
        if (uiState.recentExpenses.isEmpty()) {
            Text(
                "No expenses recorded this month.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        } else {
            uiState.recentExpenses.forEach { expense ->
                ExpenseRow(expense = expense, onDelete = {}, onEdit = {}, displayButton = false)
            }
        }
    }
}



