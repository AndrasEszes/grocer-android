package io.grocer.app.data

import java.util.concurrent.atomic.AtomicInteger

interface OrderRepository {
    suspend fun placeOrder(address: Address, totalCents: Long): Order
}

class FakeOrderRepository : OrderRepository {
    private val sequence = AtomicInteger(1041)

    override suspend fun placeOrder(address: Address, totalCents: Long): Order =
        Order(number = "GR-${sequence.incrementAndGet()}", totalCents = totalCents, address = address)
}
