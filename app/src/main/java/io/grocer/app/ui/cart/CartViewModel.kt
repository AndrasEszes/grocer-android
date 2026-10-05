package io.grocer.app.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.grocer.app.data.CartLine
import io.grocer.app.data.CartRepository
import io.grocer.app.data.PriceQuote
import io.grocer.app.data.Pricing
import io.grocer.app.data.PromoCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class CartState(
    val lines: List<CartLine> = emptyList(),
    val promoText: String = "",
    val promoError: String? = null,
    val quote: PriceQuote = PriceQuote(0, 0, 0),
)

class CartViewModel(private val cart: CartRepository) : ViewModel() {
    private val promoText = MutableStateFlow(cart.promo.value?.code.orEmpty())
    private val promoError = MutableStateFlow<String?>(null)

    val state: StateFlow<CartState> = combine(cart.lines, cart.promo, promoText, promoError) { lines, promo, text, error ->
        CartState(lines, text, error, Pricing.quote(lines, promo))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, CartState())

    fun onPromoChange(value: String) {
        promoText.value = value
        promoError.update { null }
    }

    fun applyPromo() {
        val parsed = PromoCode.parse(promoText.value)
        promoError.value = if (parsed == null) "This code is not valid" else null
        cart.applyPromo(parsed)
    }

    fun setQuantity(productId: String, quantity: Int) = cart.setQuantity(productId, quantity)
}
