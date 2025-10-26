package com.example.roseledger.data.repository

import com.example.roseledger.data.local.ExpenseDao
import com.example.roseledger.data.model.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Repository that manages expense data operations.
 * It abstracts the data source (Room database) from the rest of the app.
 *
 * @param expenseDao The Data Access Object for expenses.
 * */
class ExpenseRepository(private val expenseDao: ExpenseDao) {

    /**
     * Retrieves a real-time stream of all expenses from the database.
     * The Flow will automatically update the UI when data changes.
     */
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    /**
     * Retrieves a list of expenses within a specified date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     */
    fun getExpensesBetween(startDate: Date, endDate: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesBetweenDates(startDate, endDate)
    }

    /**
     * Inserts a new expense into the database.
     * This is a suspend function, as it's an asynchronous operation.
     *
     * @param expense The expense to be inserted.
     */
    suspend fun insert(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    /**
     * Updates an existing expense in the database.
     *
     * @param expense The updated expense data.
     */
    suspend fun update(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    /**
     * Deletes an expense from the database based on its ID.
     *
     * @param id The ID of the expense to be deleted.
     */
    suspend fun delete(id: Int) {
        expenseDao.deleteExpense(id)
    }
}