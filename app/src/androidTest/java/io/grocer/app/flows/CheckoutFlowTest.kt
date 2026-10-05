package io.grocer.app.flows

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import io.grocer.app.MainActivity
import io.grocer.app.testing.GrocerRobot
import io.grocer.app.testing.GrocerRobot.Companion.BUDAPEST_ADDRESS
import io.grocer.app.testing.TestArtifacts
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckoutFlowTest {
    @get:Rule(order = 0)
    val compose = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 1)
    val artifacts = TestArtifacts(compose)

    private val robot = GrocerRobot(compose)

    @Before
    fun signIn() = robot.signIn()

    @Test
    fun placesAnOrderWithAPromoCode() {
        robot.search("avo")
        artifacts.screenshot("search")

        robot.openProduct("avocado")
        robot.setDetailQuantity(3)
        artifacts.screenshot("product")
        robot.addToCartFromDetail()

        robot.search("")
        robot.addFromCatalog("olive-oil")
        robot.addFromCatalog("pasta")
        robot.openCart()
        artifacts.screenshot("cart")

        robot.applyPromo("FREEDELIVERY")
        compose.onNodeWithTag("cart.delivery").assertTextEquals("Free")
        compose.onNodeWithTag("cart.total").assertTextEquals("€18.95")
        artifacts.screenshot("promo-applied")

        robot.checkout()
        robot.fillAddress(BUDAPEST_ADDRESS)
        artifacts.screenshot("address")

        robot.placeOrder()
        compose.onNodeWithTag("confirmation.orderNumber").assertTextContains("GR-", substring = true)
        artifacts.screenshot("confirmation")
    }

    @Test
    fun rejectsAnInvalidPostalCode() {
        robot.addFromCatalog("croissant")
        robot.openCart()
        robot.checkout()
        robot.fillAddress(BUDAPEST_ADDRESS.copy(postalCode = "BUD-1"))
        robot.placeOrder()

        compose.onNodeWithTag("checkout.postalCode.error", useUnmergedTree = true).assertIsDisplayed()
        artifacts.screenshot("validation")
    }
}
