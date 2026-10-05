package io.grocer.app.data

enum class PromoCode(val code: String) {
    Save10("SAVE10"),
    FreeDelivery("FREEDELIVERY");

    companion object {
        fun parse(input: String): PromoCode? = entries.firstOrNull { it.code.equals(input.trim(), ignoreCase = true) }
    }
}

data class PriceQuote(
    val subtotalCents: Long,
    val discountCents: Long,
    val deliveryCents: Long,
) {
    val totalCents: Long get() = subtotalCents - discountCents + deliveryCents
}

object Pricing {
    const val DELIVERY_FEE_CENTS = 499L
    const val FREE_DELIVERY_FROM_CENTS = 5000L

    fun quote(lines: List<CartLine>, promo: PromoCode?): PriceQuote {
        val subtotal = lines.sumOf { it.totalCents }
        val discount = if (promo == PromoCode.Save10) subtotal / 10 else 0L
        val delivery = when {
            subtotal == 0L -> 0L
            promo == PromoCode.FreeDelivery -> 0L
            subtotal > FREE_DELIVERY_FROM_CENTS -> 0L
            else -> DELIVERY_FEE_CENTS
        }
        return PriceQuote(subtotal, discount, delivery)
    }
}
