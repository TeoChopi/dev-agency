package com.example.productapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data transfer object for products API response wrapper.
 * Contains the list of products and pagination information.
 */
data class ProductsResponse(
    @SerializedName("products")
    val products: List<ProductDto>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("limit")
    val limit: Int
)