package com.example.app.domain.model

/**
 * Sealed class representing different UI states for data loading operations.
 *
 * @param T The type of data being loaded
 */
sealed class UiState<out T> {
    /**
     * Initial idle state before any operation
     */
    object Idle : UiState<Nothing>()
    
    /**
     * Loading state during data fetch operations
     */
    object Loading : UiState<Nothing>()
    
    /**
     * Success state containing the loaded data
     *
     * @param data The successfully loaded data
     */
    data class Success<T>(val data: T) : UiState<T>()
    
    /**
     * Error state when an operation fails
     *
     * @param message Human-readable error message
     * @param exception Optional exception that caused the error
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : UiState<Nothing>()
}