package lucas.cordeiro.community.feature.community.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.lucascordeiro.ymir.core.utils.LifecycleUtils.ObserveActions
import lucas.cordeiro.community.feature.community.R
import lucas.cordeiro.community.feature.community.presentation.composables.CommunityEmpty
import lucas.cordeiro.community.feature.community.presentation.composables.CommunityError
import lucas.cordeiro.community.feature.community.presentation.composables.CommunityList
import lucas.cordeiro.community.feature.community.presentation.composables.CommunitySkeletonList
import lucas.cordeiro.community.shared.ui.preview.PreviewContainer
import lucas.cordeiro.community.shared.ui.preview.ThemePreviews
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CommunityScreen() {
    val viewModel = koinViewModel<CommunityViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    fun handleAction(action: CommunityUiAction) {
        when (action) {
            is CommunityUiAction.ShowError ->
                Toast.makeText(context, action.error.title, Toast.LENGTH_SHORT).show()
        }
    }

    ObserveActions(viewModel = viewModel, handleAction = ::handleAction)

    CommunityContent(
        state = state,
        onMemberClick = viewModel::clickedMember,
        onRetryClick = viewModel::clickedRetry,
        onEndReached = viewModel::reachedEnd,
    )
}

@Composable
internal fun CommunityContent(
    state: CommunityUiState,
    onMemberClick: (Int) -> Unit,
    onRetryClick: () -> Unit,
    onEndReached: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        Text(
            text = stringResource(R.string.community_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp),
        )
        when {
            state.isLoading -> CommunitySkeletonList()
            state.isError -> CommunityError(onRetryClick = onRetryClick)
            state.members.isEmpty() -> CommunityEmpty()
            else -> CommunityList(
                members = state.members,
                isLoadingNextPage = state.isLoadingNextPage,
                isNextPageError = state.isNextPageError,
                onMemberClick = onMemberClick,
                onEndReached = onEndReached,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun CommunityContentLoadingPreview() {
    PreviewContainer {
        CommunityContent(
            state = CommunityUiState(isLoading = true),
            onMemberClick = {},
            onRetryClick = {},
            onEndReached = {},
        )
    }
}

@ThemePreviews
@Composable
private fun CommunityContentErrorPreview() {
    PreviewContainer {
        CommunityContent(
            state = CommunityUiState(isLoading = false, isError = true),
            onMemberClick = {},
            onRetryClick = {},
            onEndReached = {},
        )
    }
}

@ThemePreviews
@Composable
private fun CommunityContentLoadedPreview() {
    PreviewContainer {
        CommunityContent(
            state = CommunityUiState(isLoading = false, members = CommunityPreviewData.members),
            onMemberClick = {},
            onRetryClick = {},
            onEndReached = {},
        )
    }
}

@ThemePreviews
@Composable
private fun CommunityContentEmptyPreview() {
    PreviewContainer {
        CommunityContent(
            state = CommunityUiState(isLoading = false, members = emptyList()),
            onMemberClick = {},
            onRetryClick = {},
            onEndReached = {},
        )
    }
}
