package io.grocer.app.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartRepository {
    private val _lines = MutableStateFlow<List<CartLine>>(emptyList())
    val lines: StateFlow<List<CartLine>> = _lines.asStateFlow()

    private val _promo = MutableStateFlow<PromoCode?>(null)
    val promo: StateFlow<PromoCode?> = _promo.asStateFlow()

    fun add(product: Product, quantity: Int = 1) {
        require(quantity > 0) { "quantity must be positive" }
        _lines.update { lines ->
            val existing = lines.firstOrNull { it.product.id == product.id }
            if (existing == null) {
                lines + CartLine(product, quantity)
            } else {
                lines.map { if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity) else it }
            }
        }
    }

    fun setQuantity(productId: String, quantity: Int) {
        _lines.update { lines ->
            if (quantity <= 0) {
                lines.filterNot { it.product.id == productId }
            } else {
                lines.map { if (it.product.id == productId) it.copy(quantity = quantity) else it }
            }
        }
    }

    fun applyPromo(code: PromoCode?) {
        _promo.value = code
    }

    fun clear() {
        _lines.value = emptyList()
        _promo.value = null
    }
}
