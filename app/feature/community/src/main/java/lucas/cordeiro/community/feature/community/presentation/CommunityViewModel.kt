package lucas.cordeiro.community.feature.community.presentation

import androidx.lifecycle.viewModelScope
import io.github.lucascordeiro.ymir.core.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lucas.cordeiro.community.component.community.domain.model.CommunityMember
import lucas.cordeiro.community.component.community.domain.usecase.GetCommunityMembersUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ObserveLikedMembersUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ToggleMemberLikeUseCase
import lucas.cordeiro.community.shared.core.exception.toErrorMessage

internal class CommunityViewModel(
    private val getCommunityMembersUseCase: GetCommunityMembersUseCase,
    private val toggleMemberLikeUseCase: ToggleMemberLikeUseCase,
    private val observeLikedMembersUseCase: ObserveLikedMembersUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel<CommunityUiState, CommunityUiAction>(CommunityUiState()) {

    private var rawMembers: List<CommunityMember> = emptyList()
    private var likedIds: Set<Int> = emptySet()
    private var currentPage = 0
    private var isLoadingPage = false

    init {
        observeLikes()
        loadNextPage()
    }

    private fun observeLikes() {
        viewModelScope.launch {
            observeLikedMembersUseCase().collect { ids ->
                likedIds = ids
                emitMembers()
            }
        }
    }

    fun loadNextPage() {
        if (isLoadingPage || state.value.endReached) return
        isLoadingPage = true
        viewModelScope.launch {
            val firstPage = currentPage == 0
            setState { it.copy(isLoading = firstPage, isLoadingNextPage = !firstPage, isError = false) }
            try {
                val page = withContext(ioDispatcher) { getCommunityMembersUseCase(currentPage + 1) }
                currentPage += 1
                rawMembers = rawMembers + page
                setState {
                    it.copy(
                        isLoading = false,
                        isLoadingNextPage = false,
                        endReached = page.size < PAGE_SIZE,
                    )
                }
                emitMembers()
            } catch (e: Exception) {
                val empty = rawMembers.isEmpty()
                setState { it.copy(isLoading = false, isLoadingNextPage = false, isError = empty) }
                if (!empty) sendAction { CommunityUiAction.ShowError(e.toErrorMessage()) }
            } finally {
                isLoadingPage = false
            }
        }
    }

    fun onMemberClick(id: Int) {
        viewModelScope.launch {
            withContext(ioDispatcher) { toggleMemberLikeUseCase(id) }
        }
    }

    fun retry() = loadNextPage()

    private suspend fun emitMembers() {
        val merged = rawMembers.map { member -> member.copy(isLiked = member.id in likedIds) }
        setState { it.copy(members = merged) }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
