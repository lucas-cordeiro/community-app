package lucas.cordeiro.community.feature.community.presentation

import io.github.lucascordeiro.ymir.test.ViewModelObserver
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lucas.cordeiro.community.component.community.domain.model.CommunityMember
import lucas.cordeiro.community.component.community.domain.usecase.LoadCommunityPageUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ObserveCommunityMembersUseCase
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

    private val membersFlow = MutableStateFlow<List<CommunityMember>>(emptyList())
    private val loadCommunityPageUseCase: LoadCommunityPageUseCase = mockk()
    private val toggleMemberLikeUseCase: ToggleMemberLikeUseCase = mockk(relaxed = true)
    private val observeCommunityMembersUseCase: ObserveCommunityMembersUseCase = mockk()

    init {
        every { observeCommunityMembersUseCase() } returns membersFlow
    }

    private fun stubPage(page: Int, members: List<CommunityMember>) {
        coEvery { loadCommunityPageUseCase(page) } coAnswers {
            membersFlow.value = membersFlow.value + members
            members.size
        }
    }

    private fun createViewModel() = CommunityViewModel(
        observeCommunityMembersUseCase = observeCommunityMembersUseCase,
        loadCommunityPageUseCase = loadCommunityPageUseCase,
        toggleMemberLikeUseCase = toggleMemberLikeUseCase,
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
            stubPage(1, CommunityMemberStub.list(size = 2))

            // When
            val observer = createObserver(createViewModel())
            advanceUntilIdle()

            // Then
            val state = observer.state.last()
            assertEquals(false, state.isLoading)
            assertEquals(2, state.members.size)

            observer.stop()
        }

    @Test
    fun `given full page when loading next page then appends second page`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            stubPage(1, CommunityMemberStub.list(size = 20, startId = 1))
            stubPage(2, CommunityMemberStub.list(size = 20, startId = 21))
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.reachedEnd()
            advanceUntilIdle()

            // Then
            assertEquals(40, observer.state.last().members.size)
            coVerify { loadCommunityPageUseCase(2) }

            observer.stop()
        }

    @Test
    fun `given last page smaller than page size when loading then marks end and stops paginating`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            stubPage(1, CommunityMemberStub.list(size = 5))
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.reachedEnd()
            advanceUntilIdle()

            // Then
            assertEquals(true, observer.state.last().endReached)
            coVerify(exactly = 1) { loadCommunityPageUseCase(any()) }

            observer.stop()
        }

    @Test
    fun `given liked ids emitted when observing then member is marked liked`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            stubPage(1, CommunityMemberStub.list(size = 3))
            val observer = createObserver(createViewModel())
            advanceUntilIdle()

            // When
            membersFlow.value = membersFlow.value.map { if (it.id == 2) it.copy(isLiked = true) else it }
            advanceUntilIdle()

            // Then
            assertEquals(true, observer.state.last().members.first { it.id == 2 }.isLiked)

            observer.stop()
        }

    @Test
    fun `when member clicked then toggles like`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            stubPage(1, CommunityMemberStub.list(size = 1))
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.clickedMember(1)
            advanceUntilIdle()

            // Then
            coVerify { toggleMemberLikeUseCase(1) }

            observer.stop()
        }

    @Test
    fun `given initial load failed when retry then recovers and shows members`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            val members = CommunityMemberStub.list(size = 2)
            var attempt = 0
            coEvery { loadCommunityPageUseCase(1) } coAnswers {
                attempt++
                if (attempt == 1) throw RuntimeException("boom")
                membersFlow.value = membersFlow.value + members
                members.size
            }
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.clickedRetry()
            advanceUntilIdle()

            // Then
            val state = observer.state.last()
            assertEquals(false, state.isError)
            assertEquals(2, state.members.size)

            observer.stop()
        }

    @Test
    fun `given failure on first page when initialized then shows error state without action`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // Given
            coEvery { loadCommunityPageUseCase(1) } throws RuntimeException("boom")

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
            stubPage(1, CommunityMemberStub.list(size = 20))
            coEvery { loadCommunityPageUseCase(2) } throws RuntimeException("some error")
            val viewModel = createViewModel()
            val observer = createObserver(viewModel)
            advanceUntilIdle()

            // When
            viewModel.reachedEnd()
            advanceUntilIdle()

            // Then
            val state = observer.state.last()
            assertEquals(false, state.isError)
            assertEquals(true, state.isNextPageError)
            assertEquals(20, state.members.size)
            assertTrue(observer.action.last() is CommunityUiAction.ShowError)

            observer.stop()
        }
}
