package lucas.cordeiro.community.component.community.di

import lucas.cordeiro.community.component.community.data.network.CommunityNetworkDataSource
import lucas.cordeiro.community.component.community.data.network.CommunityNetworkDataSourceImpl
import lucas.cordeiro.community.component.community.data.repository.CommunityRepositoryImpl
import lucas.cordeiro.community.component.community.data.storage.CommunityLocalDataSource
import lucas.cordeiro.community.component.community.data.storage.CommunityLocalDataSourceImpl
import lucas.cordeiro.community.component.community.domain.repository.CommunityRepository
import lucas.cordeiro.community.component.community.domain.usecase.GetCommunityMembersUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ObserveLikedMembersUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ToggleMemberLikeUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

object CommunityComponentDI {
    val module: Module = module {
        factory<CommunityNetworkDataSource> { CommunityNetworkDataSourceImpl(httpClient = get()) }
        factory<CommunityLocalDataSource> { CommunityLocalDataSourceImpl(preferenceManager = get()) }

        factory<CommunityRepository> {
            CommunityRepositoryImpl(
                networkDataSource = get(),
                localDataSource = get(),
            )
        }

        factory<GetCommunityMembersUseCase> { GetCommunityMembersUseCase(get<CommunityRepository>()::getCommunity) }
        factory<ToggleMemberLikeUseCase> { ToggleMemberLikeUseCase(get<CommunityRepository>()::toggleLike) }
        factory<ObserveLikedMembersUseCase> { ObserveLikedMembersUseCase(get<CommunityRepository>()::observeLikedIds) }
    }
}
