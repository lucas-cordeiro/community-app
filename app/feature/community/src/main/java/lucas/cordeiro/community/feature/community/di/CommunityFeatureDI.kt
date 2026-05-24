package lucas.cordeiro.community.feature.community.di

import lucas.cordeiro.community.feature.community.presentation.CommunityViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object CommunityFeatureDI {
    val module: Module = module {
        viewModel {
            CommunityViewModel(
                observeCommunityMembersUseCase = get(),
                loadCommunityPageUseCase = get(),
                toggleMemberLikeUseCase = get(),
            )
        }
    }
}
