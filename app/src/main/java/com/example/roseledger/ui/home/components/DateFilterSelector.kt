package com.example.roseledger.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.roseledger.util.DateFilter

@Composable
fun DateFilterSelector(
    currentFilter: DateFilter,
    onFilterSelected: (DateFilter) -> Unit
) {
    val options = DateFilter.entries.filter { it != DateFilter.ALL } // Exclude ALL from selector for now

    MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onCheckedChange = { onFilterSelected(option) },
                checked = option == currentFilter
            ) {
                Text(option.name.lowercase().replaceFirstChar { it.titlecase() })
            }
        }
    }
}