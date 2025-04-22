package com.pete.domain.di.usecase

import com.pete.domain.di.repository.CategoryRepository

class CategoriesUseCasse(private val repository: CategoryRepository) {
    suspend fun execute()=repository.getCategories()
}