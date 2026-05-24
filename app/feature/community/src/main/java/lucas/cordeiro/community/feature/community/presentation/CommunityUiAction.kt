package lucas.cordeiro.community.feature.community.presentation

import io.github.lucascordeiro.ymir.core.action.UiAction
import lucas.cordeiro.community.shared.core.exception.ErrorMessage

internal sealed interface CommunityUiAction : UiAction {
    data class ShowError(val error: ErrorMessage) : CommunityUiAction
}
