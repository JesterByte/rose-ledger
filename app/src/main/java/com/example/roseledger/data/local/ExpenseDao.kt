package com.example.roseledger.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.roseledger.data.model.Expense
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the Expense entity.
 * */
@Dao
interface ExpenseDao {

    /**
     * Inserts an expense into the table. If the expense already exists, it will be replaced.
     * @param expense the expense to be inserted.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    /**
     * Retrieves all expenses from the table, ordered by date in descending order.
     * Using the Flow ensures the UI is automatically updated when the data changes.
     * */
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    /**
     * Deletes an expense from the table.
     * @param id the id of the expense to be deleted.
     * */
    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpense(id: Int)
}