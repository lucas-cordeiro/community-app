package lucas.cordeiro.community.feature.community.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import lucas.cordeiro.community.shared.ui.test.robot.ScreenRobot

internal class CommunityRobot(
    composeRule: ComposeContentTestRule,
) : ScreenRobot(composeRule) {

    fun clickMember(name: String) = clickText(name)

    fun assertMemberDisplayed(name: String) = assertTextDisplayed(name)

    fun assertNewBadgeDisplayed() {
        composeRule.onAllNodesWithText("NEW").onFirst().assertIsDisplayed()
    }

    fun assertReferenceCountDisplayed(count: Int) = assertTextDisplayed(count.toString())

    fun assertErrorDisplayed() = assertTextDisplayed("Couldn't load the community.")

    fun clickRetry() = clickText("Try again")
}

internal fun ComposeContentTestRule.communityScreen(
    state: CommunityUiState,
    onMemberClick: (Int) -> Unit = {},
    onRetryClick: () -> Unit = {},
    onEndReached: () -> Unit = {},
    block: CommunityRobot.() -> Unit,
): CommunityRobot {
    setContent {
        CommunityContent(
            state = state,
            onMemberClick = onMemberClick,
            onRetryClick = onRetryClick,
            onEndReached = onEndReached,
        )
    }
    return CommunityRobot(this).apply(block)
}
