package io.grocer.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import io.grocer.app.testing.GrocerRobot
import io.grocer.app.testing.TestArtifacts
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CatalogSearchTest(private val query: String, private val expectedProductId: String) {
    @get:Rule(order = 0)
    val compose = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 1)
    val artifacts = TestArtifacts(compose)

    private val robot = GrocerRobot(compose)

    @Before
    fun signIn() = robot.signIn()

    @Test
    fun findsTheProduct() {
        robot.search(query)

        compose.onNodeWithTag("catalog.product.$expectedProductId").assertIsDisplayed()
        artifacts.screenshot("results")
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun queries() = listOf(
            arrayOf("apple", "apple-gala"),
            arrayOf("MILK", "milk"),
            arrayOf("croiss", "croissant"),
        )
    }
}
