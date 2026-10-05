package io.grocer.app

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.grocer.app.data.Category
import io.grocer.app.data.SampleCatalog
import io.grocer.app.testing.GrocerRobot
import io.grocer.app.testing.TestArtifacts
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CatalogTest {
    @get:Rule(order = 0)
    val compose = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 1)
    val artifacts = TestArtifacts(compose)

    private val robot = GrocerRobot(compose)

    @Before
    fun signIn() = robot.signIn()

    @Test
    fun searchNarrowsTheList() {
        robot.search("an")

        compose.onAllNodes(hasTestTag("catalog.product.banana")).assertCountEquals(1)
        compose.onAllNodes(hasTestTag("catalog.product.milk")).assertCountEquals(0)
        artifacts.screenshot("results")
    }

    @Test
    fun categoryFilterShowsOnlyDairy() {
        compose.onNodeWithTag("catalog.category.${Category.Dairy.name}").performClick()

        val dairy = SampleCatalog.products.filter { it.category == Category.Dairy }
        dairy.forEach { compose.onNodeWithTag("catalog.product.${it.id}").assertIsDisplayed() }
        compose.onAllNodesWithTag("catalog.product.banana").assertCountEquals(0)
        artifacts.screenshot("dairy")
    }

    @Test
    fun unknownSearchShowsTheEmptyState() {
        robot.search("dragon fruit")

        compose.onNodeWithTag("catalog.empty").assertIsDisplayed()
        artifacts.screenshot("empty-state")
    }

    @Test
    fun addingProductsUpdatesTheCartBadge() {
        robot.addFromCatalog("banana")
        robot.addFromCatalog("banana")
        robot.addFromCatalog("milk")

        compose.onNodeWithTag("catalog.cartBadge", useUnmergedTree = true).assertTextEquals("3")
        artifacts.screenshot("badge")
    }
}
