package lucas.cordeiro.community.component.community.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CommunityMemberTest {

    @Test
    fun `given referenceCnt zero then isNew is true`() {
        // Given
        val member = member(referenceCnt = 0)

        // Then
        assertEquals(true, member.isNew)
    }

    @Test
    fun `given referenceCnt greater than zero then isNew is false`() {
        // Given
        val member = member(referenceCnt = 3)

        // Then
        assertEquals(false, member.isNew)
    }

    private fun member(referenceCnt: Int) = CommunityMember(
        id = 1,
        topic = "topic",
        firstName = "Name",
        pictureUrl = "",
        natives = emptyList(),
        learns = emptyList(),
        referenceCnt = referenceCnt,
        isLiked = false,
        nationality = null,
    )
}
