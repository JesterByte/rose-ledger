package com.example.roseledger.ui.expenses.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.roseledger.data.model.Expense
import com.example.roseledger.util.formatAmountWithCommas
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A composable that displays a single expense item in a Card.
 *
 * @param expense The expense tto display.
 * @param onDelete A lambda function to be called when the delete icon is clicked.
 */
@Composable
fun ExpenseRow(
    expense: Expense,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onEdit() }, // Make the whole card clickable
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side: Description and Category/Date
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${expense.category} . ${dateFormatter.format(expense.date)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right side: Amount and Delete button
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    // Formatting the amount to two decimal places
                    text = formatAmountWithCommas(expense.amount),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Expense",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}