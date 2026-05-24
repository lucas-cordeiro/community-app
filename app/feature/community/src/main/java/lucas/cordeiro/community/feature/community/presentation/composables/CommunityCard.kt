package lucas.cordeiro.community.feature.community.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import lucas.cordeiro.community.component.community.domain.model.CommunityMember
import lucas.cordeiro.community.feature.community.R
import lucas.cordeiro.community.feature.community.presentation.CommunityPreviewData
import lucas.cordeiro.community.shared.ui.preview.PreviewContainer
import lucas.cordeiro.community.shared.ui.preview.ThemePreviews
import lucas.cordeiro.community.shared.ui.R as SharedUi

@Composable
internal fun CommunityCard(
    member: CommunityMember,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AsyncImage(
            model = member.pictureUrl,
            contentDescription = member.firstName,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(5 / 6f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                .padding(8.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.firstName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f),
                    )
                    MemberBadge(member)
                }

                Text(
                    text = member.topic,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.weight(1f))

            Row {
                Column {
                    LanguageInfo(label = stringResource(R.string.community_native), codes = member.natives)
                    LanguageInfo(label = stringResource(R.string.community_learns), codes = member.learns)
                }

                Spacer(Modifier.weight(1f))

                LikeThumb(
                    isLiked = member.isLiked,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}

@Composable
private fun MemberBadge(member: CommunityMember) {
    if (member.isNew) {
        Text(
            text = stringResource(R.string.community_badge_new),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clip(RoundedCornerShape(percent = 50))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 12.dp, vertical = 4.dp),
        )
    } else {
        Text(
            text = member.referenceCnt.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun LanguageInfo(label: String, codes: List<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = codes.joinToString(", ") { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun LikeThumb(
    isLiked: Boolean,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(if (isLiked) SharedUi.drawable.liked else SharedUi.drawable.ic_thumb_up),
        contentDescription = null,
        colorFilter = if(isLiked) null else ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = modifier.size(24.dp),
    )
}

@ThemePreviews
@Composable
private fun CommunityCardCountPreview() {
    PreviewContainer {
        CommunityCard(member = CommunityPreviewData.members[0], onClick = {})
    }
}

@ThemePreviews
@Composable
private fun CommunityCardNewPreview() {
    PreviewContainer {
        CommunityCard(member = CommunityPreviewData.members[1], onClick = {})
    }
}

@ThemePreviews
@Composable
private fun CommunityCardLikedPreview() {
    PreviewContainer {
        CommunityCard(member = CommunityPreviewData.members[2], onClick = {})
    }
}
