package com.example.productapp.presentation.product

import com.example.productapp.domain.model.Product

/**
 * Sealed class representing the UI state for the product screen.
 * Encapsulates all possible states the UI can be in.
 */
sealed class ProductUiState {
    
    /**
     * Initial state before any data loading begins.
     */
    data object Initial : ProductUiState()
    
    /**
     * Loading state when data is being fetched.
     */
    data object Loading : ProductUiState()
    
    /**
     * Success state when products are successfully loaded.
     * 
     * @property products List of products to display
     */
    data class Success(val products: List<Product>) : ProductUiState()
    
    /**
     * Error state when an error occurs during data loading.
     * 
     * @property message Error message to display to the user
     */
    data class Error(val message: String) : ProductUiState()
    
    /**
     * Empty state when no products are available.
     */
    data object Empty : ProductUiState()
}

/**
 * Extension property to check if the current state is loading.
 */
val ProductUiState.isLoading: Boolean
    get() = this is ProductUiState.Loading

/**
 * Extension property to check if the current state is success.
 */
val ProductUiState.isSuccess: Boolean
    get() = this is ProductUiState.Success

/**
 * Extension property to check if the current state is error.
 */
val ProductUiState.isError: Boolean
    get() = this is ProductUiState.Error

/**
 * Extension property to check if the current state is empty.
 */
val ProductUiState.isEmpty: Boolean
    get() = this is ProductUiState.Empty

/**
 * Extension function to get products from success state, empty list otherwise.
 */
fun ProductUiState.getProductsOrEmpty(): List<Product> {
    return if (this is ProductUiState.Success) products else emptyList()
}

/**
 * Extension function to get error message from error state, null otherwise.
 */
fun ProductUiState.getErrorMessageOrNull(): String? {
    return if (this is ProductUiState.Error) message else null
}