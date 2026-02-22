package com.example.app.data.model

/**
 * Data class representing a product in the system.
 *
 * @param id Unique identifier for the product
 * @param name Display name of the product
 * @param price Price in cents to avoid floating point precision issues
 * @param category Category the product belongs to
 * @param imageUrl URL for the product's thumbnail image
 */
data class Product(
    val id: Long,
    val name: String,
    val price: Int, // Price in cents
    val category: String,
    val imageUrl: String?
)