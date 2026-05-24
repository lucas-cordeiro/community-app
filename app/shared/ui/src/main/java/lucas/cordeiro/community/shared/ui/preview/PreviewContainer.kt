package lucas.cordeiro.community.shared.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import lucas.cordeiro.community.shared.ui.theme.CommunityTheme

@Composable
fun PreviewContainer(content: @Composable () -> Unit) {
    CommunityTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}
