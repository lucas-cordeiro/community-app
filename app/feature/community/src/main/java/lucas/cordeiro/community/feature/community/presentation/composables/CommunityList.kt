package lucas.cordeiro.community.feature.community.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lucas.cordeiro.community.component.community.domain.model.CommunityMember
import lucas.cordeiro.community.feature.community.presentation.CommunityPreviewData
import lucas.cordeiro.community.shared.ui.preview.PreviewContainer
import lucas.cordeiro.community.shared.ui.preview.ThemePreviews

@Composable
internal fun CommunityList(
    members: List<CommunityMember>,
    isLoadingNextPage: Boolean,
    onMemberClick: (Int) -> Unit,
    onLoadNextPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            info.totalItemsCount > 0 && lastVisible >= info.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadNextPage()
    }

    LazyColumn(state = listState, modifier = modifier.fillMaxSize()) {
        itemsIndexed(
            members,
            key = { _, member -> member.id },
        ) { index, member ->
            CommunityCard(
                member = member,
                onClick = { onMemberClick(member.id) },
            )
            if (index < members.lastIndex) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
        if (isLoadingNextPage) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
        }
        item {
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}

private const val LOAD_MORE_THRESHOLD = 3

@ThemePreviews
@Composable
private fun CommunityListPreview() {
    PreviewContainer {
        CommunityList(
            members = CommunityPreviewData.members,
            isLoadingNextPage = false,
            onMemberClick = {},
            onLoadNextPage = {},
        )
    }
}
