package lucas.cordeiro.community.component.community.data.storage

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
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
    fun `given id not liked when toggle then transform adds it atomically`() = runTest {
        // Given
        val transform = slot<(Set<String>) -> Set<String>>()
        coEvery { preferenceManager.updateStringSet(any(), capture(transform)) } returns Unit

        // When
        dataSource.toggleLike(5)

        // Then
        coVerify { preferenceManager.updateStringSet(any(), any()) }
        assertEquals(setOf("5"), transform.captured(emptySet()))
    }

    @Test
    fun `given id already liked when toggle then transform removes it atomically`() = runTest {
        // Given
        val transform = slot<(Set<String>) -> Set<String>>()
        coEvery { preferenceManager.updateStringSet(any(), capture(transform)) } returns Unit

        // When
        dataSource.toggleLike(5)

        // Then
        assertEquals(emptySet<String>(), transform.captured(setOf("5")))
    }

    @Test
    fun `given observed ids when observeLikedIds then maps to ints ignoring invalid`() = runTest {
        // Given
        every { preferenceManager.observeStringSet(any()) } returns flowOf(setOf("3", "4", "x"))

        // When
        val result = dataSource.observeLikedIds().first()

        // Then
        assertEquals(setOf(3, 4), result)
    }
}
