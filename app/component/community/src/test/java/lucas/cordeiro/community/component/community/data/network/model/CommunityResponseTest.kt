package lucas.cordeiro.community.component.community.data.network.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CommunityResponseTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `member response is deserialized honoring serial names`() {
        // Given
        val raw =
            """{"id":5,"topic":"t","firstName":"Ana","pictureUrl":"u","natives":["es","pt"],"learns":["en"],"referenceCnt":7}"""

        // When
        val member = json.decodeFromString<CommunityMemberResponse>(raw)

        // Then
        assertEquals(5, member.id)
        assertEquals("Ana", member.firstName)
        assertEquals(7, member.referenceCnt)
        assertEquals(listOf("es", "pt"), member.natives)
        assertEquals(listOf("en"), member.learns)
    }
}
