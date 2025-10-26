package com.example.roseledger.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roseledger.data.preferences.ThemeSetting

@Composable
fun ThemeSelectionRow(
    currentSetting: ThemeSetting,
    onSettingSelected: (ThemeSetting) -> Unit
) {
    val options = ThemeSetting.entries

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Theme",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onCheckedChange = { onSettingSelected(option) },
                    checked = option == currentSetting
                ) {
                    Text(option.name.lowercase().replaceFirstChar { it.titlecase() })
                }
            }
        }
    }
}