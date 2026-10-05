package io.grocer.app.testing

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import io.grocer.app.data.Address
import io.grocer.app.data.FakeAuthRepository

class GrocerRobot(private val compose: ComposeTestRule) {

    fun enterCredentials(email: String = FakeAuthRepository.DEMO_EMAIL, password: String = FakeAuthRepository.DEMO_PASSWORD) {
        compose.onNodeWithTag("login.email").performTextInput(email)
        compose.onNodeWithTag("login.password").performTextInput(password)
    }

    fun signIn() {
        enterCredentials()
        compose.onNodeWithTag("login.submit").performClick()
        compose.waitUntil(5_000) { compose.onAllNodes(hasTestTag("catalog.search")).fetchSemanticsNodes().isNotEmpty() }
    }

    fun search(query: String) {
        compose.onNodeWithTag("catalog.search").performTextReplacement(query)
    }

    fun addFromCatalog(productId: String) {
        scrollCatalogTo(productId)
        compose.onNodeWithTag("catalog.add.$productId").performClick()
    }

    fun openProduct(productId: String) {
        scrollCatalogTo(productId)
        compose.onNodeWithTag("catalog.product.$productId").performClick()
        compose.onNodeWithTag("product.addToCart").assertIsDisplayed()
    }

    fun setDetailQuantity(quantity: Int) {
        repeat(quantity - 1) { compose.onNodeWithTag("product.increase").performClick() }
    }

    fun addToCartFromDetail() {
        compose.onNodeWithTag("product.addToCart").performClick()
    }

    fun openCart() {
        compose.onNodeWithTag("catalog.cart").performClick()
    }

    fun applyPromo(code: String) {
        compose.onNodeWithTag("cart.promo").performTextReplacement(code)
        compose.onNodeWithTag("cart.applyPromo").performClick()
    }

    fun checkout() {
        compose.onNodeWithTag("cart.checkout").performClick()
    }

    fun fillAddress(address: Address) {
        compose.onNodeWithTag("checkout.name").performTextInput(address.fullName)
        compose.onNodeWithTag("checkout.street").performTextInput(address.street)
        compose.onNodeWithTag("checkout.city").performTextInput(address.city)
        compose.onNodeWithTag("checkout.postalCode").performTextInput(address.postalCode)
    }

    fun placeOrder() {
        compose.onNodeWithTag("checkout.placeOrder").performClick()
    }

    private fun scrollCatalogTo(productId: String) {
        compose.onNodeWithTag("catalog.list").performScrollToNode(hasTestTag("catalog.product.$productId"))
    }

    companion object {
        val BUDAPEST_ADDRESS = Address("Anna Kovács", "Andrássy út 12", "Budapest", "1061")
    }
}
