package com.example.app.domain.repository

import com.example.app.data.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for product data operations.
 * Defines the contract for accessing product data from various sources.
 */
interface ProductRepository {
    /**
     * Retrieves all products as a Flow for reactive updates.
     *
     * @return Flow emitting Result containing list of products or error
     */
    fun getAllProducts(): Flow<Result<List<Product>>>
    
    /**
     * Refreshes the product data from remote source.
     *
     * @return Result indicating success or failure of the refresh operation
     */
    suspend fun refreshProducts(): Result<Unit>
}