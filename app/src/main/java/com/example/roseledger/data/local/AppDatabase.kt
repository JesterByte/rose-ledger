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
import java.util.concurrent.TimeUnit
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

    private class AppDatabaseCallback : RoomDatabase.Callback() {

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

            // Add sample expenses
            expenseDao.insertExpense(
                Expense(
                    amount = 75.50,
                    category = "Groceries",
                    description = "Weekly shopping at a supermarket",
                    date = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2))
                )
            )
            expenseDao.insertExpense(
                Expense(
                    amount = 12.00,
                    category = "Transport",
                    description = "Train ticket to downtown",
                    date = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3))
                )
            )
            expenseDao.insertExpense(
                Expense(
                    amount = 45.00,
                    category = "Bills",
                    description = "Monthly internet bill",
                    date = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5))
                )
            )
            expenseDao.insertExpense(
                Expense(
                    amount = 25.00,
                    category = "Food",
                    description = "Lunch with a colleague",
                    date = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
                )
            )
            expenseDao.insertExpense(
                Expense(
                    amount = 250.00,
                    category = "Shopping",
                    description = "New jacket",
                    date = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10))
                )
            )
        }
    }
}