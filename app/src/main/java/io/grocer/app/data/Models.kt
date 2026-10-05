package io.grocer.app.data

enum class Category(val title: String) {
    Fruit("Fruit"),
    Vegetables("Vegetables"),
    Dairy("Dairy"),
    Bakery("Bakery"),
    Pantry("Pantry"),
}

data class Product(
    val id: String,
    val name: String,
    val category: Category,
    val priceCents: Long,
    val unit: String,
    val emoji: String,
    val description: String,
)

data class CartLine(val product: Product, val quantity: Int) {
    val totalCents: Long get() = product.priceCents * quantity
}

data class Address(
    val fullName: String,
    val street: String,
    val city: String,
    val postalCode: String,
)

data class Order(val number: String, val totalCents: Long, val address: Address)
