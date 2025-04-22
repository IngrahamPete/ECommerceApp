package com.pete.data.di.repository

import com.pete.domain.di.model.CategoriesListModel
import com.pete.domain.di.network.NetworkService
import com.pete.domain.di.network.ResultWrapper
import com.pete.domain.di.repository.CategoryRepository

class CategoryRepositoryImp(val networkService: NetworkService): CategoryRepository {
    override suspend fun getCategories(): ResultWrapper<CategoriesListModel> {
        return networkService.getCategories()
    }




}