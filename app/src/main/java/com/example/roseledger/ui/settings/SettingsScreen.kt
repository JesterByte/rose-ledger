package com.example.roseledger.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roseledger.RoseLedgerApplication
import com.example.roseledger.ui.settings.components.CategoryManagementSection
import com.example.roseledger.ui.settings.components.ThemeSelectionRow
import com.example.roseledger.ui.viewmodel.SettingsViewModel
import com.example.roseledger.ui.viewmodel.SettingsViewModelFactory

@Composable
fun SettingsScreen() {
    val context = LocalContext.current.applicationContext
    val application = context as RoseLedgerApplication
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(
            themePreferences = application.themePreferences,
            categoryRepository = application.categoryRepository
        )
    )

    val themeSetting by viewModel.themeSetting.collectAsState()
    val categories by viewModel.categories.collectAsState()

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            ThemeSelectionRow(
                currentSetting = themeSetting,
                onSettingSelected = { viewModel.setTheme(it) }
            )
        }

        item {
            CategoryManagementSection(
                categories = categories,
                onAddCategory = { viewModel.addCategory(it) },
                onDeleteCategory = { viewModel.deleteCategory(it) }
            )
        }
    }

}



