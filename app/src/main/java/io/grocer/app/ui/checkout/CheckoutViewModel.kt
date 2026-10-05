package io.grocer.app.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.grocer.app.data.Address
import io.grocer.app.data.AddressField
import io.grocer.app.data.AddressValidator
import io.grocer.app.data.CartRepository
import io.grocer.app.data.Order
import io.grocer.app.data.OrderRepository
import io.grocer.app.data.Pricing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CheckoutState(
    val address: Address = Address("", "", "", ""),
    val invalidFields: Set<AddressField> = emptySet(),
    val totalCents: Long = 0,
    val placing: Boolean = false,
    val placedOrder: Order? = null,
)

class CheckoutViewModel(private val cart: CartRepository, private val orders: OrderRepository) : ViewModel() {
    private val _state = MutableStateFlow(
        CheckoutState(totalCents = Pricing.quote(cart.lines.value, cart.promo.value).totalCents),
    )
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    fun onAddressChange(address: Address) = _state.update { it.copy(address = address, invalidFields = emptySet()) }

    fun placeOrder() {
        val current = _state.value
        val invalid = AddressValidator.validate(current.address)
        if (invalid.isNotEmpty()) {
            _state.update { it.copy(invalidFields = invalid) }
            return
        }
        _state.update { it.copy(placing = true) }
        viewModelScope.launch {
            val order = orders.placeOrder(current.address, current.totalCents)
            cart.clear()
            _state.update { it.copy(placing = false, placedOrder = order) }
        }
    }
}
