package lucas.cordeiro.community.feature.community.presentation

import io.github.lucascordeiro.ymir.test.ViewModelObserver
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lucas.cordeiro.community.component.community.domain.usecase.GetCommunityMembersUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ObserveLikedMembersUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ToggleMemberLikeUseCase
import lucas.cordeiro.community.feature.community.stubs.CommunityMemberStub
import lucas.cordeiro.community.shared.ui.test.viewmodel.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val getCommunityMembersUseCase: GetCommunityMembersUseCase = mockk()
    private val toggleMemberLikeUseCase: ToggleMemberLikeUseCase = mockk(relaxed = true)
    private val observeLikedMembersUseCase: ObserveLikedMembersUseCase = mockk()

    private fun createViewModel() = CommunityViewModel(
        getCommunityMembersUseCase = getCommunityMembersUseCase,
        toggleMemberLikeUseCase = toggleMemberLikeUseCase,
        observeLikedMembersUseCase = observeLikedMembersUseCase,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    private fun createObserver(
        viewModel: CommunityViewModel,
    ): ViewModelObserver<CommunityUiState, CommunityUiAction, CommunityViewModel> {
        val observer = ViewModelObserver(
            viewModel = viewModel,
            testDispatcher = mainDispatcherRule.testDispatcher,
        )
        observer.start()
        return observer
    }

    @Test
    fun `given members returned when initialized then loads first page and stops loading`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            val members = CommunityMemberStub.list(size = 2)
            coEvery { getCommunityMembersUseCase(1) } returns members
            every { observeLikedMembersUseCase() } returns flowOf(emptySet())

            // When
            val observer = createObserver(createViewModel())
            advanceUntilIdle()

            // Then
            val state = observer.state.last()
            assertEquals(false, state.isLoading)
            assertEquals(members, state.members)

            observer.stop()
        }

    @Test
    fun `given full page when loading next page then appends second page`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            val page1 = CommunityMemberStub.list(size = 20, startId = 1)
            val page2 = CommunityMemberStub.list(size = 20, startId = 21)
            coEvery { getCommunityMembersUseCase(1) } returns page1
            coEvery { getCommunityMembersUseCase(2) } returns page2
            every { observeLikedMembersUseCase() } returns flowOf(emptySet())
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.loadNextPage()
            advanceUntilIdle()

            // Then
            assertEquals(40, observer.state.last().members.size)
            coVerify { getCommunityMembersUseCase(2) }

            observer.stop()
        }

    @Test
    fun `given last page smaller than page size when loading then marks end and stops paginating`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            coEvery { getCommunityMembersUseCase(1) } returns CommunityMemberStub.list(size = 5)
            every { observeLikedMembersUseCase() } returns flowOf(emptySet())
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.loadNextPage()
            advanceUntilIdle()

            // Then
            assertEquals(true, observer.state.last().endReached)
            coVerify(exactly = 1) { getCommunityMembersUseCase(any()) }

            observer.stop()
        }

    @Test
    fun `given liked ids emitted when observing then member is marked liked`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            val likedFlow = MutableStateFlow<Set<Int>>(emptySet())
            coEvery { getCommunityMembersUseCase(1) } returns CommunityMemberStub.list(size = 3)
            every { observeLikedMembersUseCase() } returns likedFlow
            val observer = createObserver(createViewModel())
            advanceUntilIdle()

            // When
            likedFlow.value = setOf(2)
            advanceUntilIdle()

            // Then
            assertEquals(true, observer.state.last().members.first { it.id == 2 }.isLiked)

            observer.stop()
        }

    @Test
    fun `when member clicked then toggles like`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            coEvery { getCommunityMembersUseCase(1) } returns CommunityMemberStub.list(size = 1)
            every { observeLikedMembersUseCase() } returns flowOf(emptySet())
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.onMemberClick(1)
            advanceUntilIdle()

            // Then
            coVerify { toggleMemberLikeUseCase(1) }

            observer.stop()
        }

    @Test
    fun `given failure on first page when initialized then shows error state without action`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            coEvery { getCommunityMembersUseCase(1) } throws RuntimeException("boom")
            every { observeLikedMembersUseCase() } returns flowOf(emptySet())

            // When
            val observer = createObserver(createViewModel())
            advanceUntilIdle()

            // Then
            val state = observer.state.last()
            assertEquals(false, state.isLoading)
            assertEquals(true, state.isError)
            assertEquals(0, observer.action.size)

            observer.stop()
        }

    @Test
    fun `given failure on next page when paginating then emits error action and keeps list`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            coEvery { getCommunityMembersUseCase(1) } returns CommunityMemberStub.list(size = 20)
            coEvery { getCommunityMembersUseCase(2) } throws RuntimeException("some error")
            every { observeLikedMembersUseCase() } returns flowOf(emptySet())
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.loadNextPage()
            advanceUntilIdle()

            // Then
            val state = observer.state.last()
            assertEquals(false, state.isError)
            assertEquals(20, state.members.size)
            assertTrue(observer.action.last() is CommunityUiAction.ShowError)

            observer.stop()
        }
}
