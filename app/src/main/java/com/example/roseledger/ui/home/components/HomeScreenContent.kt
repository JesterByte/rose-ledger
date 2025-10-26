package com.example.roseledger.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roseledger.ui.viewmodel.HomeUiState
import com.example.roseledger.util.DateFilter

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onFilterChange: (DateFilter) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Make the column scrollable
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Filter selection UI
        DateFilterSelector(
         currentFilter = uiState.currentFilter,
            onFilterSelected = onFilterChange
        )

        // Card for displaying total expenses
        TotalExpenseCard(total = uiState.totalExpenses, dateRange = uiState.dateRangeLabel)

        // Card for displaying expenses by category
        CategorySummaryCard(categories = uiState.expensesByCategory)
    }
}