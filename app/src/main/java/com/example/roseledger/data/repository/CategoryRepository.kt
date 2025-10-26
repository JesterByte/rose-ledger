package com.example.roseledger.data.repository

import com.example.roseledger.data.local.CategoryDao
import com.example.roseledger.data.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insert(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun delete(id: Int) {
        categoryDao.deleteCategoryById(id)
    }
}