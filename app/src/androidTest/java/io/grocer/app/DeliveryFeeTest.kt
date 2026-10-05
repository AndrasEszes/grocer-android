package io.grocer.app

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import io.grocer.app.testing.GrocerRobot
import io.grocer.app.testing.KnownIssue
import io.grocer.app.testing.TestArtifacts
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeliveryFeeTest {
    @get:Rule(order = 0)
    val compose = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 1)
    val artifacts = TestArtifacts(compose)

    private val robot = GrocerRobot(compose)

    @Before
    fun signIn() = robot.signIn()

    @Test
    fun deliveryIsFreeAboveFiftyEuros() {
        addCoffee(quantity = 5)

        compose.onNodeWithTag("cart.subtotal").assertTextEquals("€62.50")
        compose.onNodeWithTag("cart.delivery").assertTextEquals("Free")
        artifacts.screenshot("free-delivery")
    }

    @Test
    @KnownIssue("Orders of exactly €50.00 are still charged for delivery")
    fun deliveryIsFreeFromExactlyFiftyEuros() {
        addCoffee(quantity = 4)

        compose.onNodeWithTag("cart.subtotal").assertTextEquals("€50.00")
        artifacts.screenshot("cart")
        compose.onNodeWithTag("cart.delivery").assertTextEquals("Free")
    }

    private fun addCoffee(quantity: Int) {
        robot.openProduct("coffee")
        robot.setDetailQuantity(quantity)
        robot.addToCartFromDetail()
        robot.openCart()
    }
}
