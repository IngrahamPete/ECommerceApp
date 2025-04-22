package com.pete.domain.di.repository

import com.pete.domain.di.model.CategoriesListModel
import com.pete.domain.di.network.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories(): ResultWrapper<CategoriesListModel>
}

