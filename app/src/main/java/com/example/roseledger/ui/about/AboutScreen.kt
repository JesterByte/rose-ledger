package com.example.roseledger.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.roseledger.ui.about.components.TechListItem

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "About Rose Ledger",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Rose Ledger is a simple, modern expense tracker designed to provide clear insights into your spending habits. Built entirely with modern Android development tools.",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Concept and Idea by Eugine Jose & Sherry Rose Pajares.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tools & Libraries Section
        Text(
            text = "Tools & Libraries Used",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                TechListItem("Kotlin", "The primary programming language.")
                TechListItem("Jetpack Compose", "The modern declarative UI toolkit for Android.")
                TechListItem("Material 3", "The design system for UI components.")
                TechListItem("MVVM Architecture", "For a scalable and maintainable app structure.")
                TechListItem("Jetpack Navigation", "For navigating between screens.")
                TechListItem("Room", "For local SQLite database storage.")
                TechListItem("Coroutines & Flow", "For asynchronous programming and reactive data streams.")
                TechListItem("DataStore", "For storing user preferences like the app theme.")
                TechListItem("Vico", "A third-party library for creating beautiful charts.")
            }
        }
    }
}