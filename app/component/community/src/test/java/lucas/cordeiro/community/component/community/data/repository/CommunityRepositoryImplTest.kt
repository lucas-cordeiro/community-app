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

    private fun repository() = CommunityRepositoryImpl(networkDataSource, localDataSource)

    @Test
    fun `given a page when loadPage then returns count and members emits mapped list`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.responseOf(size = 3)
        every { localDataSource.observeLikedIds() } returns flowOf(emptySet())
        val repository = repository()

        // When
        val count = repository.loadPage(1)

        // Then
        assertEquals(3, count)
        assertEquals(listOf(1, 2, 3), repository.members.first().map { it.id })
    }

    @Test
    fun `given liked ids when members observed then merges isLiked`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.responseOf(size = 3)
        every { localDataSource.observeLikedIds() } returns flowOf(setOf(2))
        val repository = repository()
        repository.loadPage(1)

        // When
        val members = repository.members.first()

        // Then
        assertEquals(false, members.first { it.id == 1 }.isLiked)
        assertEquals(true, members.first { it.id == 2 }.isLiked)
        assertEquals(false, members.first { it.id == 3 }.isLiked)
    }

    @Test
    fun `given multiple pages when loadPage then accumulates members`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.responseOf(size = 20, startId = 1)
        coEvery { networkDataSource.getCommunity(2) } returns CommunityResponseStub.responseOf(size = 5, startId = 21)
        every { localDataSource.observeLikedIds() } returns flowOf(emptySet())
        val repository = repository()

        // When
        repository.loadPage(1)
        val secondCount = repository.loadPage(2)

        // Then
        assertEquals(5, secondCount)
        assertEquals(25, repository.members.first().size)
    }

    @Test
    fun `given first native when members then maps nationality from it`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.response(
            listOf(CommunityResponseStub.member(id = 1, natives = listOf("en", "de"))),
        )
        every { localDataSource.observeLikedIds() } returns flowOf(emptySet())
        val repository = repository()
        repository.loadPage(1)

        // When / Then
        assertEquals("gb", repository.members.first().first().nationality)
    }

    @Test
    fun `given empty natives when members then nationality is null`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.response(
            listOf(CommunityResponseStub.member(id = 1, natives = emptyList())),
        )
        every { localDataSource.observeLikedIds() } returns flowOf(emptySet())
        val repository = repository()
        repository.loadPage(1)

        // When / Then
        assertNull(repository.members.first().first().nationality)
    }

    @Test
    fun `given known native languages when members then maps each to its country`() = runTest {
        // Given
        val expectedByLanguage = mapOf(
            "en" to "gb", "de" to "de", "es" to "es", "it" to "it",
            "ru" to "ru", "pt" to "br", "ja" to "jp", "ko" to "kr",
            "fr" to "fr", "zh" to "cn", "nl" to "nl", "tr" to "tr", "pl" to "pl",
        )
        every { localDataSource.observeLikedIds() } returns flowOf(emptySet())

        expectedByLanguage.forEach { (language, expectedCountry) ->
            coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.response(
                listOf(CommunityResponseStub.member(id = 1, natives = listOf(language))),
            )
            val repository = repository()
            repository.loadPage(1)

            // When
            val nationality = repository.members.first().first().nationality

            // Then
            assertEquals(expectedCountry, nationality)
        }
    }

    @Test
    fun `given unknown native language when members then nationality is null`() = runTest {
        // Given
        coEvery { networkDataSource.getCommunity(1) } returns CommunityResponseStub.response(
            listOf(CommunityResponseStub.member(id = 1, natives = listOf("xx"))),
        )
        every { localDataSource.observeLikedIds() } returns flowOf(emptySet())
        val repository = repository()
        repository.loadPage(1)

        // When / Then
        assertNull(repository.members.first().first().nationality)
    }

    @Test
    fun `when toggleLike then delegates to local data source`() = runTest {
        // Given
        val repository = repository()

        // When
        repository.toggleLike(7)

        // Then
        coVerify { localDataSource.toggleLike(7) }
    }
}
