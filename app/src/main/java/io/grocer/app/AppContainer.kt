package io.grocer.app

import io.grocer.app.data.AuthRepository
import io.grocer.app.data.CartRepository
import io.grocer.app.data.FakeAuthRepository
import io.grocer.app.data.FakeOrderRepository
import io.grocer.app.data.OrderRepository

class AppContainer(
    val auth: AuthRepository = FakeAuthRepository(),
    val cart: CartRepository = CartRepository(),
    val orders: OrderRepository = FakeOrderRepository(),
)
