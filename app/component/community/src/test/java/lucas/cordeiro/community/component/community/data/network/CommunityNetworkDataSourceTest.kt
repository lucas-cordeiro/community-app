package lucas.cordeiro.community.component.community.data.network

import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.test.runTest
import lucas.cordeiro.community.shared.network.test.HttpClientMock
import lucas.cordeiro.community.shared.network.test.error
import lucas.cordeiro.community.shared.network.test.success
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CommunityNetworkDataSourceTest {

    @Test
    fun `given page when getCommunity then requests json file and parses members`() = runTest {
        // Given
        val json = """
            {"response":[
              {"id":1,"topic":"topic","firstName":"Tobi","pictureUrl":"pic","natives":["de","ja"],"learns":["en","pt"],"referenceCnt":0}
            ]}
        """.trimIndent()
        val client = HttpClientMock.handler { request ->
            if(request.url.toString() == "https://tandem2019.web.app/api/community_1.json") {
                success(json)
            } else {
                error()
            }
        }
        val dataSource = CommunityNetworkDataSourceImpl(client)

        // When
        val result = dataSource.getCommunity(1)

        // Then
        assertEquals(1, result.response.size)
        val member = result.response.first()
        assertEquals("Tobi", member.firstName)
        assertEquals(0, member.referenceCnt)
        assertEquals(listOf("de", "ja"), member.natives)
        assertEquals(listOf("en", "pt"), member.learns)
    }

    @Test
    fun `given another page when getCommunity then requests that page file`() = runTest {
        // Given
        val client = HttpClientMock.handler { request ->
            if(request.url.toString() == "https://tandem2019.web.app/api/community_3.json") {
                success("""{"response":[]}""")
            } else {
                error()
            }
        }

        // When
        val response = CommunityNetworkDataSourceImpl(client).getCommunity(3)

        // Then
        assertTrue(response.response.isEmpty())
    }

    @Test
    fun `given server error when getCommunity then throws`() = runTest {
        // Given
        val client = HttpClientMock.handler { error() }
        val dataSource = CommunityNetworkDataSourceImpl(client)

        // When
        val error = runCatching { dataSource.getCommunity(1) }.exceptionOrNull()

        // Then
        assertTrue(error is ServerResponseException)
    }
}
