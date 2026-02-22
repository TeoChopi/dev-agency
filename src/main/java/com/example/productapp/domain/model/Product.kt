package com.example.productapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model representing a product.
 * 
 * @property id Unique identifier for the product
 * @property title Product title/name
 * @property description Product description
 * @property price Product price
 * @property discountPercentage Discount percentage applied to the product
 * @property rating Product rating
 * @property stock Available stock quantity
 * @property brand Product brand
 * @property category Product category
 * @property thumbnail Thumbnail image URL
 * @property images List of product image URLs
 */
@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
) : Parcelable