package io.grocer.app.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.grocer.app.data.CartRepository
import io.grocer.app.data.Category
import io.grocer.app.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CatalogState(
    val query: String = "",
    val category: Category? = null,
    val products: List<Product> = emptyList(),
    val cartCount: Int = 0,
)

class CatalogViewModel(private val allProducts: List<Product>, private val cart: CartRepository) : ViewModel() {
    private val query = MutableStateFlow("")
    private val category = MutableStateFlow<Category?>(null)

    val state: StateFlow<CatalogState> = combine(query, category, cart.lines) { query, category, lines ->
        CatalogState(
            query = query,
            category = category,
            products = allProducts.filter { product ->
                (category == null || product.category == category) &&
                    (query.isBlank() || product.name.contains(query.trim(), ignoreCase = true))
            },
            cartCount = lines.sumOf { it.quantity },
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, CatalogState(products = allProducts))

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun onCategorySelected(value: Category?) {
        category.value = if (category.value == value) null else value
    }

    fun addToCart(product: Product) = cart.add(product)
}
