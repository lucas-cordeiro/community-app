package lucas.cordeiro.community.feature.community.presentation

import androidx.lifecycle.viewModelScope
import io.github.lucascordeiro.ymir.core.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lucas.cordeiro.community.component.community.domain.usecase.LoadCommunityPageUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ObserveCommunityMembersUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ToggleMemberLikeUseCase
import lucas.cordeiro.community.shared.core.exception.toErrorMessage

internal class CommunityViewModel(
    private val observeCommunityMembersUseCase: ObserveCommunityMembersUseCase,
    private val loadCommunityPageUseCase: LoadCommunityPageUseCase,
    private val toggleMemberLikeUseCase: ToggleMemberLikeUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel<CommunityUiState, CommunityUiAction>(CommunityUiState()) {

    private var currentPage = 0
    private var isLoadingPage = false

    init {
        observeMembers()
        loadNextPage()
    }

    private fun observeMembers() {
        viewModelScope.launch {
            observeCommunityMembersUseCase().collect { members ->
                setState { it.copy(members = members) }
            }
        }
    }

    private fun loadNextPage() {
        if (isLoadingPage || state.value.endReached) return
        isLoadingPage = true
        viewModelScope.launch {
            val firstPage = currentPage == 0
            setState {
                it.copy(
                    isLoading = firstPage,
                    isLoadingNextPage = !firstPage,
                    isError = false,
                    isNextPageError = false,
                )
            }
            try {
                val count = withContext(ioDispatcher) { loadCommunityPageUseCase(currentPage + 1) }
                currentPage += 1
                setState {
                    it.copy(
                        isLoading = false,
                        isLoadingNextPage = false,
                        endReached = count < PAGE_SIZE,
                    )
                }
            } catch (e: Exception) {
                val empty = state.value.members.isEmpty()
                setState {
                    it.copy(
                        isLoading = false,
                        isLoadingNextPage = false,
                        isError = empty,
                        isNextPageError = !empty,
                    )
                }
                if (!empty) sendAction { CommunityUiAction.ShowError(e.toErrorMessage()) }
            } finally {
                isLoadingPage = false
            }
        }
    }

    fun clickedMember(id: Int) {
        viewModelScope.launch {
            withContext(ioDispatcher) { toggleMemberLikeUseCase(id) }
        }
    }

    fun reachedEnd() = loadNextPage()

    fun clickedRetry() = loadNextPage()

    private companion object {
        const val PAGE_SIZE = 20
    }
}
