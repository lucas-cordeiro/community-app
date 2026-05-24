package lucas.cordeiro.community.feature.community.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import lucas.cordeiro.community.shared.ui.preview.PreviewContainer
import lucas.cordeiro.community.shared.ui.preview.ThemePreviews

@Composable
internal fun CommunityCardSkeleton(modifier: Modifier = Modifier) {
    val placeholder = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp)
            .shimmer(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(5 / 6f)
                .clip(RoundedCornerShape(12.dp))
                .background(placeholder),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeletonLine(widthFraction = 0.5f, placeholder = placeholder, height = 22.dp)
                SkeletonLine(widthFraction = 0.95f, placeholder = placeholder, height = 16.dp)
                SkeletonLine(widthFraction = 0.7f, placeholder = placeholder, height = 16.dp)
            }
            SkeletonLine(widthFraction = 0.55f, placeholder = placeholder, height = 14.dp)
        }
    }
}

@Composable
private fun SkeletonLine(
    widthFraction: Float,
    placeholder: Color,
    height: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(placeholder),
    )
}

@ThemePreviews
@Composable
private fun CommunityCardSkeletonPreview() {
    PreviewContainer {
        CommunityCardSkeleton()
    }
}
