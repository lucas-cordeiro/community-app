package lucas.cordeiro.community.feature.community.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import lucas.cordeiro.community.shared.ui.preview.PreviewContainer
import lucas.cordeiro.community.shared.ui.preview.ThemePreviews

private const val SKELETON_ITEMS = 6

@Composable
internal fun CommunitySkeletonList(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        repeat(SKELETON_ITEMS) {
            CommunityCardSkeleton()
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}

@ThemePreviews
@Composable
private fun CommunitySkeletonListPreview() {
    PreviewContainer {
        CommunitySkeletonList()
    }
}
