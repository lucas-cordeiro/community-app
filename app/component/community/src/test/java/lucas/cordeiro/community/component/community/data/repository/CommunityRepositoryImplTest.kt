package lucas.cordeiro.community.component.community.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import lucas.cordeiro.community.component.community.data.network.CommunityNetworkDataSource
import lucas.cordeiro.community.component.community.data.storage.CommunityLocalDataSource
import lucas.cordeiro.community.component.community.stubs.CommunityResponseStub
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityRepositoryImplTest {

    private val networkDataSource: CommunityNetworkDataSource = mockk()
    private val localDataSource: CommunityLocalDataSource = mockk(relaxed = true)
    private val repository = CommunityRepositoryImpl(networkDataSource, localDataSource)

    @Test
    fun `given liked ids when getCommunity then merges isLiked into members`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.responseOf(size = 3)
        coEvery { localDataSource.getLikedIds() } returns setOf(2)

        // When
        val result = repository.getCommunity(1)

        // Then
        assertEquals(false, result.first { it.id == 1 }.isLiked)
        assertEquals(true, result.first { it.id == 2 }.isLiked)
        assertEquals(false, result.first { it.id == 3 }.isLiked)
    }

    @Test
    fun `given first native when getCommunity then maps nationality from it`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.response(
            listOf(CommunityResponseStub.member(id = 1, natives = listOf("en", "de"))),
        )
        coEvery { localDataSource.getLikedIds() } returns emptySet()

        // When
        val result = repository.getCommunity(1)

        // Then
        assertEquals("gb", result.first().nationality)
    }

    @Test
    fun `given empty natives when getCommunity then nationality is null`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.response(
            listOf(CommunityResponseStub.member(id = 1, natives = emptyList())),
        )
        coEvery { localDataSource.getLikedIds() } returns emptySet()

        // When
        val result = repository.getCommunity(1)

        // Then
        assertNull(result.first().nationality)
    }

    @Test
    fun `given known native languages when getCommunity then maps each to its country`() = runTest {
        // Given
        val expectedByLanguage = mapOf(
            "en" to "gb", "de" to "de", "es" to "es", "it" to "it",
            "ru" to "ru", "pt" to "br", "ja" to "jp", "ko" to "kr",
            "fr" to "fr", "zh" to "cn", "nl" to "nl", "tr" to "tr", "pl" to "pl",
        )
        coEvery { localDataSource.getLikedIds() } returns emptySet()

        expectedByLanguage.forEach { (language, expectedCountry) ->
            coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.response(
                listOf(CommunityResponseStub.member(id = 1, natives = listOf(language))),
            )

            // When
            val nationality = repository.getCommunity(1).first().nationality

            // Then
            assertEquals(expectedCountry, nationality)
        }
    }

    @Test
    fun `given unknown native language when getCommunity then nationality is null`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.response(
            listOf(CommunityResponseStub.member(id = 1, natives = listOf("xx"))),
        )
        coEvery { localDataSource.getLikedIds() } returns emptySet()

        // When
        val result = repository.getCommunity(1)

        // Then
        assertNull(result.first().nationality)
    }

    @Test
    fun `when toggleLike then delegates to local data source`() = runTest {
        // When
        repository.toggleLike(7)

        // Then
        coVerify { localDataSource.toggleLike(7) }
    }

    @Test
    fun `when observeLikedIds then returns ids from local data source`() = runTest {
        // Given
        every { localDataSource.observeLikedIds() } returns flowOf(setOf(1, 2))

        // When
        val result = repository.observeLikedIds().first()

        // Then
        assertEquals(setOf(1, 2), result)
    }
}
