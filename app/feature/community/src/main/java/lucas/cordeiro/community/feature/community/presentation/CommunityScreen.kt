package lucas.cordeiro.community.feature.community.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import lucas.cordeiro.community.shared.ui.preview.PreviewContainer
import lucas.cordeiro.community.shared.ui.preview.ThemePreviews

@Composable
fun CommunityScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Community",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@ThemePreviews
@Composable
private fun CommunityScreenPreview() {
    PreviewContainer {
        CommunityScreen()
    }
}
