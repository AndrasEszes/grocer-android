package io.grocer.app.data

import org.junit.Assert.assertEquals
import org.junit.Test

class PricingTest {
    private val coffee = SampleCatalog.byId("coffee")!!
    private val milk = SampleCatalog.byId("milk")!!

    @Test
    fun `an empty cart costs nothing`() {
        assertEquals(PriceQuote(0, 0, 0), Pricing.quote(emptyList(), promo = null))
    }

    @Test
    fun `small orders pay the delivery fee`() {
        val quote = Pricing.quote(listOf(CartLine(milk, 2)), promo = null)

        assertEquals(278, quote.subtotalCents)
        assertEquals(Pricing.DELIVERY_FEE_CENTS, quote.deliveryCents)
        assertEquals(777, quote.totalCents)
    }

    @Test
    fun `large orders get free delivery`() {
        val quote = Pricing.quote(listOf(CartLine(coffee, 5)), promo = null)

        assertEquals(0, quote.deliveryCents)
        assertEquals(6250, quote.totalCents)
    }

    @Test
    fun `SAVE10 takes ten percent off the subtotal`() {
        val quote = Pricing.quote(listOf(CartLine(coffee, 2)), PromoCode.Save10)

        assertEquals(250, quote.discountCents)
        assertEquals(2500 - 250 + Pricing.DELIVERY_FEE_CENTS, quote.totalCents)
    }

    @Test
    fun `FREEDELIVERY waives the delivery fee`() {
        val quote = Pricing.quote(listOf(CartLine(milk, 1)), PromoCode.FreeDelivery)

        assertEquals(0, quote.deliveryCents)
    }

    @Test
    fun `promo codes are case and whitespace insensitive`() {
        assertEquals(PromoCode.Save10, PromoCode.parse("  save10 "))
        assertEquals(null, PromoCode.parse("SAVE20"))
    }
}
