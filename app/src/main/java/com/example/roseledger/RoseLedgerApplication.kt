package com.example.roseledger

import android.app.Application
import com.example.roseledger.data.local.AppDatabase
import com.example.roseledger.data.preferences.ThemePreferences
import com.example.roseledger.data.repository.CategoryRepository
import com.example.roseledger.data.repository.ExpenseRepository

/**
 * The Application class for the Rose Ledger app.
 * It provides a single instance of the database and repository for the entire app.
 */
class RoseLedgerApplication : Application() {

    // Lazily initialize the database. It will be created only when first accessed.
    private val database by lazy { AppDatabase.getDatabase(this) }

    // Lazily initialize the repository, using the database's DAO.
    val expenseRepository by lazy { ExpenseRepository(database.expenseDao()) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }

    val themePreferences by lazy { ThemePreferences(this) }
}