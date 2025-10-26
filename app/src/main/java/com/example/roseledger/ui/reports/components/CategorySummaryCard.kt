package com.example.roseledger.ui.reports.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CategorySummaryCard(categories: Map<String, Double>, totalExpenses: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Summary by Category",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (categories.isEmpty()) {
                Text(
                    text = "No expenses recorded yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // Sort categories by amount, descending
                val sortedCategories = categories.entries.sortedByDescending { it.value }

                sortedCategories.forEach { (category, amount) ->
                    val percentage = if (totalExpenses > 0) (amount / totalExpenses).toFloat() else 0f
                    CategoryRow(
                        category = category,
                        amount = amount,
                        percentage = percentage
                    )
                }
            }
        }
    }
}