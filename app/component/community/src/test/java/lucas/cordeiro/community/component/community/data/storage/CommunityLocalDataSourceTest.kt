package lucas.cordeiro.community.component.community.data.storage

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import lucas.cordeiro.community.shared.storage.preference.PreferenceManager
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityLocalDataSourceTest {

    private val preferenceManager: PreferenceManager = mockk(relaxed = true)
    private val dataSource = CommunityLocalDataSourceImpl(preferenceManager)

    @Test
    fun `given id not liked when toggle then adds it`() = runTest {
        // Given
        coEvery { preferenceManager.getStringSet(any()) } returns emptySet()

        // When
        dataSource.toggleLike(5)

        // Then
        coVerify { preferenceManager.setStringSet(any(), setOf("5")) }
    }

    @Test
    fun `given id already liked when toggle then removes it`() = runTest {
        // Given
        coEvery { preferenceManager.getStringSet(any()) } returns setOf("5")

        // When
        dataSource.toggleLike(5)

        // Then
        coVerify { preferenceManager.setStringSet(any(), emptySet()) }
    }

    @Test
    fun `given stored ids when getLikedIds then maps to ints ignoring invalid`() = runTest {
        // Given
        coEvery { preferenceManager.getStringSet(any()) } returns setOf("1", "2", "x")

        // When
        val result = dataSource.getLikedIds()

        // Then
        assertEquals(setOf(1, 2), result)
    }

    @Test
    fun `given observed ids when observeLikedIds then maps to ints`() = runTest {
        // Given
        every { preferenceManager.observeStringSet(any()) } returns flowOf(setOf("3", "4"))

        // When
        val result = dataSource.observeLikedIds().first()

        // Then
        assertEquals(setOf(3, 4), result)
    }
}
