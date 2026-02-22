package com.example.productapp.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productapp.domain.repository.ProductRepository
import com.example.productapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the product screen.
 * Manages the UI state and handles business logic for product operations.
 * 
 * @property repository The product repository for data operations
 */
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Initial)
    
    /**
     * StateFlow exposing the current UI state.
     */
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
    
    init {
        loadProducts()
    }
    
    /**
     * Loads products from the repository.
     * Updates the UI state based on the result.
     * 
     * @param limit Number of products to fetch (default: 30)
     * @param skip Number of products to skip for pagination (default: 0)
     */
    fun loadProducts(limit: Int = 30, skip: Int = 0) {
        repository.getProducts(limit = limit, skip = skip)
            .onEach { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> ProductUiState.Loading
                    is Result.Success -> ProductUiState.Success(result.data)
                    is Result.Error -> ProductUiState.Error(result.message)
                    is Result.Empty -> ProductUiState.Empty
                }
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * Refreshes the product list.
     * Reloads products from the beginning.
     */
    fun refreshProducts() {
        loadProducts()
    }
    
    /**
     * Retries loading products after an error.
     */
    fun retryLoadProducts() {
        loadProducts()
    }
    
    /**
     * Loads more products for pagination.
     * 
     * @param currentSize Current number of products loaded
     * @param pageSize Number of products to load per page (default: 30)
     */
    fun loadMoreProducts(currentSize: Int, pageSize: Int = 30) {
        viewModelScope.launch {
            val currentProducts = uiState.value.getProductsOrEmpty()
            
            repository.getProducts(limit = pageSize, skip = currentSize)
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val allProducts = currentProducts + result.data
                            _uiState.value = ProductUiState.Success(allProducts)
                        }
                        is Result.Error -> {
                            // Keep current products and show error for loading more
                            _uiState.value = ProductUiState.Error(result.message)
                        }
                        else -> {
                            // For loading and empty states during pagination,
                            // we might want to show a different UI indication
                        }
                    }
                }
        }
    }
}