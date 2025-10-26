package com.example.roseledger.ui.expenses.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchBar(
    searchQuery: String,
    onValueChange: (String) -> Unit
) {
    // Search bar
    TextField(
        value = searchQuery,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search by category or description") }
    )
}