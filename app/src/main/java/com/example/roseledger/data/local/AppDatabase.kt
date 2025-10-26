package com.example.roseledger.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roseledger.data.model.Category
import com.example.roseledger.data.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@Database(entities = [Expense::class, Category::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        // The volatile annotation ensures that the INSTANCE variable is always up-to-date and visible to all threads.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Return the existing instance if it's not null, otherwise create a new database instance.
            // The synchronized block ensures that only one thread can execute the database creation code at a time.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rose_ledger_database"
                ).addCallback(AppDatabaseCallback()).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class AppDatabaseCallback : Callback() {

        /**
         * Override the onCreate method to populate the database.
         */
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                // Use a coroutine to perform the database operations off the main thread
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.expenseDao(), database.categoryDao())
                }
            }
        }

        /**
         * Populates the database with sample expense data.
         */
        suspend fun populateDatabase(expenseDao: ExpenseDao, categoryDao: CategoryDao) {
            val defaultCategories = listOf("Groceries", "Transport", "Bills", "Food", "Shopping", "Entertainment")
            defaultCategories.forEach { categoryDao.insertCategory(Category(name = it)) }

            // Helper to create dates relative to today
            fun getDate(dayOfMonth: Int, monthOffset: Int = 0, yearOffset: Int = 0): Date {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.YEAR, -yearOffset)
                calendar.add(Calendar.MONTH, -monthOffset)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                return calendar.time
            }

            // --- Current Year Data ---

            // This Month
            expenseDao.insertExpense(Expense(amount = 15.50, category = "Food", description = "Coffee and pastry", date = getDate(24)))
            expenseDao.insertExpense(Expense(amount = 22.00, category = "Transport", description = "Ride share", date = getDate(23)))
            expenseDao.insertExpense(Expense(amount = 89.30, category = "Groceries", description = "Weekly groceries", date = getDate(20)))
            expenseDao.insertExpense(Expense(amount = 40.00, category = "Entertainment", description = "Movie tickets", date = getDate(18)))
            expenseDao.insertExpense(Expense(amount = 120.00, category = "Bills", description = "Electricity Bill", date = getDate(10)))
            expenseDao.insertExpense(Expense(amount = 55.75, category = "Shopping", description = "New shirt", date = getDate(8)))
            expenseDao.insertExpense(Expense(amount = 35.00, category = "Health", description = "Pharmacy", date = getDate(5)))
            expenseDao.insertExpense(Expense(amount = 8.50, category = "Food", description = "Morning coffee", date = getDate(2)))

            // Last Month
            expenseDao.insertExpense(Expense(amount = 250.00, category = "Shopping", description = "New headphones", date = getDate(15, 1)))
            expenseDao.insertExpense(Expense(amount = 115.00, category = "Bills", description = "Internet Bill (Prev)", date = getDate(10, 1)))
            expenseDao.insertExpense(Expense(amount = 75.20, category = "Groceries", description = "Groceries", date = getDate(5, 1)))

            // Two Months Ago
            expenseDao.insertExpense(Expense(amount = 300.00, category = "Travel", description = "Flight booking", date = getDate(20, 2)))
            expenseDao.insertExpense(Expense(amount = 95.00, category = "Entertainment", description = "Concert", date = getDate(15, 2)))

            // --- Last Year Data ---
            // Spread data across last year to make the year chart interesting.
            expenseDao.insertExpense(Expense(amount = 450.00, category = "Shopping", description = "Winter Coat", date = getDate(10, 1, 1))) // Last year, 1 month ago from today
            expenseDao.insertExpense(Expense(amount = 110.00, category = "Bills", description = "Gas Bill", date = getDate(15, 2, 1)))
            expenseDao.insertExpense(Expense(amount = 80.00, category = "Health", description = "Dentist Check-up", date = getDate(20, 3, 1)))
            expenseDao.insertExpense(Expense(amount = 210.00, category = "Groceries", description = "Holiday Groceries", date = getDate(22, 4, 1)))
            expenseDao.insertExpense(Expense(amount = 150.00, category = "Entertainment", description = "Theater show", date = getDate(1, 5, 1)))
            expenseDao.insertExpense(Expense(amount = 65.00, category = "Transport", description = "Monthly pass", date = getDate(5, 6, 1)))
            expenseDao.insertExpense(Expense(amount = 45.00, category = "Food", description = "Dinner out", date = getDate(12, 7, 1)))
            expenseDao.insertExpense(Expense(amount = 800.00, category = "Travel", description = "Hotel for vacation", date = getDate(18, 8, 1)))
            expenseDao.insertExpense(Expense(amount = 95.00, category = "Shopping", description = "New shoes", date = getDate(25, 9, 1)))
            expenseDao.insertExpense(Expense(amount = 50.00, category = "Bills", description = "Water Bill", date = getDate(10, 10, 1)))
            expenseDao.insertExpense(Expense(amount = 130.00, category = "Groceries", description = "Stocking up pantry", date = getDate(5, 11, 1)))
        }
    }
}