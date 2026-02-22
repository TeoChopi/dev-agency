package com.example.productapp.data.mapper

import com.example.productapp.data.remote.dto.ProductDto
import com.example.productapp.domain.model.Product

/**
 * Extension function to convert ProductDto to Product domain model.
 * 
 * @return [Product] domain model
 */
fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images
    )
}

/**
 * Extension function to convert a list of ProductDto to a list of Product domain models.
 * 
 * @return List of [Product] domain models
 */
fun List<ProductDto>.toDomain(): List<Product> {
    return map { it.toDomain() }
}