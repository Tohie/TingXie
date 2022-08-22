package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Categories
import com.example.tingxie.domain.model.CategoriesWithCharacters
import com.example.tingxie.domain.model.CharacterCategoryCrossRef
import com.example.tingxie.domain.model.InvalidCategoryException
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class CategoryUseCases(
    private val repository: CharacterRepository
) {
    fun getCategories(): Flow<List<CategoriesWithCharacters>> {
        return repository.getCategories()
    }

    suspend fun getCategory(categoryId: Int): Categories? {
        return repository.getCategory(categoryId)
    }


    @Throws(InvalidCategoryException::class)
    suspend fun insertCategory(categories: Categories) {
        if (categories.categoryName.isBlank()) throw InvalidCategoryException("Category name can't be blank")
        repository.insertCategory(categories)
    }

    suspend fun addCharacterToCategory(characterCategory: CharacterCategoryCrossRef) {
        repository.addCharacterToCategory(characterCategory)
    }

    suspend fun deleteCharacterFromCategory(characterCategory: CharacterCategoryCrossRef) {
        repository.deleteCharacterFromCategory(characterCategory)
    }
}