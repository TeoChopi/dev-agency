package com.example.productapp.util

/**
 * Sealed class representing different states of data operations.
 * Used to handle loading, success, error, and empty states consistently across the app.
 * 
 * @param T The type of data being handled
 */
sealed class Result<out T> {
    
    /**
     * Represents a loading state.
     */
    data object Loading : Result<Nothing>()
    
    /**
     * Represents a successful operation with data.
     * 
     * @property data The successful result data
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents an error state with an error message.
     * 
     * @property message The error message describing what went wrong
     */
    data class Error(val message: String) : Result<Nothing>()
    
    /**
     * Represents an empty state (successful operation but no data).
     */
    data object Empty : Result<Nothing>()
}

/**
 * Returns true if the result is Loading.
 */
val Result<*>.isLoading: Boolean
    get() = this is Result.Loading

/**
 * Returns true if the result is Success.
 */
val Result<*>.isSuccess: Boolean
    get() = this is Result.Success

/**
 * Returns true if the result is Error.
 */
val Result<*>.isError: Boolean
    get() = this is Result.Error

/**
 * Returns true if the result is Empty.
 */
val Result<*>.isEmpty: Boolean
    get() = this is Result.Empty

/**
 * Returns the data if the result is Success, null otherwise.
 */
fun <T> Result<T>.getDataOrNull(): T? {
    return if (this is Result.Success) data else null
}

/**
 * Returns the error message if the result is Error, null otherwise.
 */
fun Result<*>.getErrorOrNull(): String? {
    return if (this is Result.Error) message else null
}