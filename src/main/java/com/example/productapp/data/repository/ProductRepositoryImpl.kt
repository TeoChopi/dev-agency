package com.example.productapp.data.repository

import com.example.productapp.data.mapper.toDomain
import com.example.productapp.data.remote.api.ProductApi
import com.example.productapp.domain.model.Product
import com.example.productapp.domain.repository.ProductRepository
import com.example.productapp.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [ProductRepository] that fetches products from a remote API.
 * 
 * @property api The product API service
 */
@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {
    
    override fun getProducts(limit: Int, skip: Int): Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.Loading)
            
            val response = api.getProducts(limit = limit, skip = skip)
            val products = response.products.toDomain()
            
            if (products.isEmpty()) {
                emit(Result.Empty)
            } else {
                emit(Result.Success(products))
            }
        } catch (e: HttpException) {
            emit(Result.Error(getHttpErrorMessage(e.code())))
        } catch (e: IOException) {
            emit(Result.Error("Network error. Please check your internet connection."))
        } catch (e: Exception) {
            emit(Result.Error("An unexpected error occurred: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Maps HTTP error codes to user-friendly error messages.
     * 
     * @param code HTTP status code
     * @return User-friendly error message
     */
    private fun getHttpErrorMessage(code: Int): String {
        return when (code) {
            400 -> "Bad request. Please try again."
            401 -> "Unauthorized access. Please check your credentials."
            403 -> "Access forbidden."
            404 -> "Products not found."
            500 -> "Server error. Please try again later."
            502 -> "Bad gateway. Please try again later."
            503 -> "Service unavailable. Please try again later."
            else -> "HTTP error $code occurred."
        }
    }
}