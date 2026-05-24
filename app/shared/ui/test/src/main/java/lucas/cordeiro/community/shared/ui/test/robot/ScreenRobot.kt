package lucas.cordeiro.community.shared.ui.test.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

abstract class ScreenRobot(
    protected val composeRule: ComposeContentTestRule,
) {
    fun assertTextDisplayed(text: String) {
        composeRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun assertTextDoesNotExist(text: String) {
        composeRule.onNodeWithText(text).assertDoesNotExist()
    }

    fun clickText(text: String) {
        composeRule.onNodeWithText(text).performClick()
    }
}
