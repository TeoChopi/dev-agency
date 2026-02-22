package com.example.productapp.data.remote.api

import com.example.productapp.data.remote.dto.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for product-related network requests.
 */
interface ProductApi {
    
    /**
     * Fetches products from the API.
     * 
     * @param limit Number of products to fetch (default: 30)
     * @param skip Number of products to skip for pagination (default: 0)
     * @return [ProductsResponse] containing the list of products and pagination info
     */
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0
    ): ProductsResponse
}