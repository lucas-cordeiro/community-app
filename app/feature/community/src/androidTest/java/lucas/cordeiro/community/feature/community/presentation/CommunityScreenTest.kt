package lucas.cordeiro.community.feature.community.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommunityScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun clickingMemberCardTriggersOnMemberClick() {
        // Given
        var clickedId: Int? = null

        // When
        composeRule.communityScreen(
            state = CommunityUiState(isLoading = false, members = CommunityPreviewData.members),
            onMemberClick = { clickedId = it },
        ) {
            clickMember("Jonathan")
        }

        // Then
        assertEquals(1, clickedId)
    }

    @Test
    fun loadedStateRendersNameCountAndNewBadge() {
        // Given
        val state = CommunityUiState(isLoading = false, members = CommunityPreviewData.members)

        // When / Then
        composeRule.communityScreen(state = state) {
            assertMemberDisplayed("Jonathan")
            assertReferenceCountDisplayed(12)
            assertNewBadgeDisplayed()
        }
    }

    @Test
    fun loadingStateDoesNotShowMembers() {
        // Given
        val state = CommunityUiState(isLoading = true)

        // When / Then
        composeRule.communityScreen(state = state) {
            assertTextDoesNotExist("Jonathan")
        }
    }

    @Test
    fun errorStateShowsRetryAndTriggersOnRetry() {
        // Given
        var retried = false

        // When
        composeRule.communityScreen(
            state = CommunityUiState(isLoading = false, isError = true),
            onRetryClick = { retried = true },
        ) {
            assertErrorDisplayed()
            clickRetry()
        }

        // Then
        assertTrue(retried)
    }
}
