package com.example.roseledger.ui.expenses.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.roseledger.data.model.Category
import com.example.roseledger.data.model.Expense
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDialog(
    expenseToEdit: Expense? = null, // Optional expense for editing
    categories: List<Category>,
    onDismissRequest: () -> Unit,
    onConfirm: (amount: Double, description: String, category: String, date: Date) -> Unit
) {
    val isEditMode = expenseToEdit != null
    val title = if (isEditMode) "Edit Expense" else "Add New Expense"
    val confirmButtonText = if (isEditMode) "Save" else "Add"

    // Use 'remember' with a key to re-initialize state when the expense changes
    var amount by remember(expenseToEdit) { mutableStateOf(expenseToEdit?.amount?.toString() ?: "") }
    var description by remember(expenseToEdit) { mutableStateOf(expenseToEdit?.description ?: "") }
    var selectedCategory by remember(expenseToEdit) {
        mutableStateOf(categories.find { it.name == expenseToEdit?.category })
    }

    var isAmountInvalid by remember { mutableStateOf(false) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }


    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        isAmountInvalid = false // Reset validation on change
                    },
                    label = { Text("Amount")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isAmountInvalid,
                    singleLine = true
                )
                if (isAmountInvalid) {
                    Text("Please enter a valid amount.", color = MaterialTheme.colorScheme.error)
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = isCategoryDropdownExpanded,
                    onExpandedChange = { isCategoryDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isCategoryDropdownExpanded,
                        onDismissRequest = { isCategoryDropdownExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    isCategoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    val finalCategory = selectedCategory

                    if (amountDouble != null && description.isNotBlank() && finalCategory != null) {
                        // Pass back the original date in edit mode, or a new date in add mode
                        val date = expenseToEdit?.date ?: Date()
                        onConfirm(amountDouble, description, finalCategory.name, date)
                    } else {
                        isAmountInvalid = amountDouble == null
                    }
                }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}