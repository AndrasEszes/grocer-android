package io.grocer.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.grocer.app.testing.GrocerRobot
import io.grocer.app.testing.TestArtifacts
import org.junit.Rule
import org.junit.Test

class LoginTest {
    @get:Rule(order = 0)
    val compose = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 1)
    val artifacts = TestArtifacts(compose)

    private val robot = GrocerRobot(compose)

    @Test
    fun signsInWithTheDemoAccount() {
        robot.enterCredentials()
        artifacts.screenshot("filled-form")

        compose.onNodeWithTag("login.submit").performClick()

        compose.onNodeWithTag("catalog.search").assertIsDisplayed()
        artifacts.screenshot("catalog")
    }

    @Test
    fun showsAnErrorForAWrongPassword() {
        robot.enterCredentials(password = "wrong-password")
        compose.onNodeWithTag("login.submit").performClick()

        compose.onNodeWithTag("login.error").assertTextEquals("Wrong email or password")
        artifacts.screenshot("error")
    }

    @Test
    fun signInIsDisabledUntilBothFieldsAreFilled() {
        compose.onNodeWithTag("login.submit").assertIsNotEnabled()
        artifacts.screenshot("empty-form")
    }
}
