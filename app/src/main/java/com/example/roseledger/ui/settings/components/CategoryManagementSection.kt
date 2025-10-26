package com.example.roseledger.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roseledger.data.model.Category
import kotlin.collections.forEach

@Composable
fun CategoryManagementSection(
    categories: List<Category>,
    onAddCategory: (String) -> Unit,
    onDeleteCategory: (Int) -> Unit
) {
    var newCategoryName by remember { mutableStateOf("") }

    Column {
        Text(
            "Manage Categories",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Input to add a new category
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newCategoryName,
                onValueChange = { newCategoryName = it },
                label = { Text("New Category Name") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            IconButton(onClick = {
                onAddCategory(newCategoryName)
                newCategoryName = "" // Clear input after adding
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }

        // List of existing categories
        categories.forEach { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(category.name, style = MaterialTheme.typography.bodyLarge)
                IconButton(onClick = { onDeleteCategory(category.id) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Category",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}