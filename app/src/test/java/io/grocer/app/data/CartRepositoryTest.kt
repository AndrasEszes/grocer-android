package io.grocer.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CartRepositoryTest {
    private val cart = CartRepository()
    private val banana = SampleCatalog.byId("banana")!!
    private val bread = SampleCatalog.byId("sourdough")!!

    @Test
    fun `adding the same product twice merges the lines`() {
        cart.add(banana)
        cart.add(banana, quantity = 2)

        assertEquals(listOf(CartLine(banana, 3)), cart.lines.value)
    }

    @Test
    fun `setting the quantity to zero removes the line`() {
        cart.add(banana)
        cart.add(bread)

        cart.setQuantity(banana.id, 0)

        assertEquals(listOf(CartLine(bread, 1)), cart.lines.value)
    }

    @Test
    fun `clearing the cart drops the promo code too`() {
        cart.add(banana)
        cart.applyPromo(PromoCode.Save10)

        cart.clear()

        assertTrue(cart.lines.value.isEmpty())
        assertNull(cart.promo.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adding a non-positive quantity is rejected`() {
        cart.add(banana, quantity = 0)
    }
}
