package com.example.productapp.domain.repository

import com.example.productapp.domain.model.Product
import com.example.productapp.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for product data operations.
 * Defines the contract for accessing product data from various sources.
 */
interface ProductRepository {
    
    /**
     * Fetches products from the data source.
     * 
     * @param limit Number of products to fetch
     * @param skip Number of products to skip for pagination
     * @return [Flow] of [Result] containing the list of products or error
     */
    fun getProducts(limit: Int = 30, skip: Int = 0): Flow<Result<List<Product>>>
}