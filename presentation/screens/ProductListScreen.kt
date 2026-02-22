package com.example.app.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.R
import com.example.app.data.model.Product
import com.example.app.domain.model.UiState
import com.example.app.presentation.components.ProductItem
import com.example.app.presentation.viewmodel.ProductListViewModel

/**
 * Main product list screen composable that displays products in a scrollable list.
 *
 * @param modifier Modifier for customizing the layout
 * @param viewModel ViewModel for managing screen state and business logic
 * @param onSearchClick Callback when search icon is clicked
 * @param onProductClick Callback when a product item is clicked
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel(),
    onSearchClick: () -> Unit = {},
    onProductClick: (Product) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refreshProducts
    )
    
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Buscar productos"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(paddingValues)
        ) {
            when (uiState) {
                is UiState.Idle -> {
                    // Initial state - could show skeleton loading or empty content
                }
                
                is UiState.Loading -> {
                    LoadingContent()
                }
                
                is UiState.Success -> {
                    if (uiState.data.isEmpty()) {
                        EmptyContent()
                    } else {
                        ProductListContent(
                            products = uiState.data,
                            onProductClick = onProductClick
                        )
                    }
                }
                
                is UiState.Error -> {
                    ErrorContent(
                        message = uiState.message,
                        onRetryClick = viewModel::retry
                    )
                }
            }
            
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

/**
 * Composable for displaying the product list content.
 *
 * @param products List of products to display
 * @param onProductClick Callback when a product is clicked
 */
@Composable
private fun ProductListContent(
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = products,
            key = { product -> product.id }
        ) { product ->
            ProductItem(
                product = product,
                onClick = { onProductClick(product) }
            )
        }
    }
}

/**
 * Composable for displaying loading state with centered spinner.
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Composable for displaying error state with retry functionality.
 *
 * @param message Error message to display
 * @param onRetryClick Callback when retry button is clicked
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onRetryClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Reintentar")
        }
    }
}

/**
 * Composable for displaying empty state when no products are available.
 */
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay productos disponibles",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListScreenLoadingPreview() {
    MaterialTheme {
        LoadingContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListScreenEmptyPreview() {
    MaterialTheme {
        EmptyContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListScreenErrorPreview() {
    MaterialTheme {
        ErrorContent(
            message = "Error al cargar productos. Verifica tu conexión a internet.",
            onRetryClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListContentPreview() {
    val sampleProducts = listOf(
        Product(
            id = 1,
            name = "iPhone 14 Pro",
            price = 119900,
            category = "Smartphones",
            imageUrl = null
        ),
        Product(
            id = 2,
            name = "Samsung Galaxy S23",
            price = 89900,
            category = "Smartphones",
            imageUrl = null
        ),
        Product(
            id = 3,
            name = "MacBook Pro 14\"",
            price = 219900,
            category = "Laptops",
            imageUrl = null
        )
    )
    
    MaterialTheme {
        ProductListContent(
            products = sampleProducts,
            onProductClick = {}
        )
    }
}