package lucas.cordeiro.community.feature.community.presentation

import io.github.lucascordeiro.ymir.core.state.UiState
import lucas.cordeiro.community.component.community.domain.model.CommunityMember

internal data class CommunityUiState(
    val isLoading: Boolean = true,
    val isLoadingNextPage: Boolean = false,
    val isError: Boolean = false,
    val endReached: Boolean = false,
    val members: List<CommunityMember> = emptyList(),
) : UiState
