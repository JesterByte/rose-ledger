package com.example.roseledger.ui.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roseledger.RoseLedgerApplication
import com.example.roseledger.ui.reports.components.CategorySummaryCard
import com.example.roseledger.ui.reports.components.DateFilterSelector
import com.example.roseledger.ui.reports.components.TotalExpenseCard
import com.example.roseledger.ui.viewmodel.ReportsViewModel
import com.example.roseledger.ui.viewmodel.ReportsViewModelFactory

@Composable
fun ReportsScreen() {
    val context = LocalContext.current.applicationContext
    val application = context as RoseLedgerApplication
    val viewModel: ReportsViewModel = viewModel(
        factory = ReportsViewModelFactory(
            application.expenseRepository
        )
    )
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        DateFilterSelector(
            currentFilter = uiState.currentFilter,
            onFilterSelected = { viewModel.setDateFilter(it) }
        )
        Spacer(Modifier.height(16.dp))

        TotalExpenseCard(
            total = uiState.totalExpenses,
            dateRange = uiState.dateRangeLabel,
            chartData = uiState.chartData,
            currentFilter = uiState.currentFilter
        )
        Spacer(Modifier.height(16.dp))

        CategorySummaryCard(categories = uiState.expensesByCategory, totalExpenses = uiState.totalExpenses)

        // Other KPI cards here
    }
}