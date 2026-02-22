package com.example.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.model.Product
import com.example.app.domain.model.UiState
import com.example.app.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing product list screen state and business logic.
 *
 * @param productRepository Repository for product data operations
 */
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Product>>> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadProducts()
    }

    /**
     * Loads products from the repository.
     * Updates UI state based on the result.
     */
    fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .onStart { _uiState.value = UiState.Loading }
                .catch { exception ->
                    _uiState.value = UiState.Error(
                        message = "Error al cargar productos: ${exception.message}",
                        exception = exception
                    )
                }
                .collect { result ->
                    _uiState.value = result.fold(
                        onSuccess = { products ->
                            if (products.isEmpty()) {
                                UiState.Success(emptyList())
                            } else {
                                UiState.Success(products)
                            }
                        },
                        onFailure = { exception ->
                            UiState.Error(
                                message = "Error al cargar productos: ${exception.message}",
                                exception = exception
                            )
                        }
                    )
                }
        }
    }

    /**
     * Refreshes product data from remote source.
     * Used for pull-to-refresh functionality.
     */
    fun refreshProducts() {
        viewModelScope.launch {
            _isRefreshing.value = true
            
            productRepository.refreshProducts()
                .fold(
                    onSuccess = {
                        // Products will be automatically updated through the Flow
                    },
                    onFailure = { exception ->
                        _uiState.value = UiState.Error(
                            message = "Error al actualizar productos: ${exception.message}",
                            exception = exception
                        )
                    }
                )
            
            _isRefreshing.value = false
        }
    }

    /**
     * Retries loading products after an error state.
     */
    fun retry() {
        loadProducts()
    }
}