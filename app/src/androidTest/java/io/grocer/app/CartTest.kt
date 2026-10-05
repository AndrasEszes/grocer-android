package io.grocer.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.grocer.app.testing.GrocerRobot
import io.grocer.app.testing.TestArtifacts
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartTest {
    @get:Rule(order = 0)
    val compose = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 1)
    val artifacts = TestArtifacts(compose)

    private val robot = GrocerRobot(compose)

    @Before
    fun fillCart() {
        robot.signIn()
        robot.addFromCatalog("strawberry")
        robot.addFromCatalog("sourdough")
        robot.openCart()
    }

    @Test
    fun smallOrdersPayForDelivery() {
        compose.onNodeWithTag("cart.subtotal").assertTextEquals("€8.99")
        compose.onNodeWithTag("cart.delivery").assertTextEquals("€4.99")
        compose.onNodeWithTag("cart.total").assertTextEquals("€13.98")
        artifacts.screenshot("summary")
    }

    @Test
    fun save10TakesTenPercentOff() {
        robot.applyPromo("save10")

        compose.onNodeWithTag("cart.discount").assertTextEquals("−€0.89")
        compose.onNodeWithTag("cart.total").assertTextEquals("€13.09")
        artifacts.screenshot("discount")
    }

    @Test
    fun anUnknownPromoCodeIsRejected() {
        robot.applyPromo("FREEBEER")

        compose.onNodeWithTag("cart.promoError", useUnmergedTree = true).assertTextEquals("This code is not valid")
        artifacts.screenshot("error")
    }

    @Test
    fun removingTheLastItemEmptiesTheCart() {
        compose.onNodeWithTag("cart.decrease.strawberry").performClick()
        artifacts.screenshot("one-item-left")
        compose.onNodeWithTag("cart.decrease.sourdough").performClick()

        compose.onNodeWithTag("cart.empty").assertIsDisplayed()
        artifacts.screenshot("empty")
    }
}
