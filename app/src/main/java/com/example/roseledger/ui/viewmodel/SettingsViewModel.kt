package com.example.roseledger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roseledger.data.model.Category
import com.example.roseledger.data.preferences.ThemePreferences
import com.example.roseledger.data.preferences.ThemeSetting
import com.example.roseledger.data.repository.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themePreferences: ThemePreferences,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // Expose the theme preferences as a StateFlow for the UI to collect
    val themeSetting = themePreferences.themeSetting
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ThemeSetting.SYSTEM // Default initial value
        )

    // Function to be called by the UI to change the theme
    fun setTheme(theme: ThemeSetting) {
        viewModelScope.launch {
            themePreferences.setThemeSetting(theme)
        }
    }

    val categories = categoryRepository.allCategories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun addCategory(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                categoryRepository.insert(Category(name = name.trim()))
            }
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            categoryRepository.delete(id)
        }
    }
}

class SettingsViewModelFactory(
    private val themePreferences: ThemePreferences,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(themePreferences, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}