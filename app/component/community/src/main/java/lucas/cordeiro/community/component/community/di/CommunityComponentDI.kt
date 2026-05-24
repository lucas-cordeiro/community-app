package lucas.cordeiro.community.component.community.di

import lucas.cordeiro.community.component.community.data.network.CommunityNetworkDataSource
import lucas.cordeiro.community.component.community.data.network.CommunityNetworkDataSourceImpl
import lucas.cordeiro.community.component.community.data.repository.CommunityRepositoryImpl
import lucas.cordeiro.community.component.community.data.storage.CommunityLocalDataSource
import lucas.cordeiro.community.component.community.data.storage.CommunityLocalDataSourceImpl
import lucas.cordeiro.community.component.community.domain.repository.CommunityRepository
import lucas.cordeiro.community.component.community.domain.usecase.LoadCommunityPageUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ObserveCommunityMembersUseCase
import lucas.cordeiro.community.component.community.domain.usecase.ToggleMemberLikeUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

object CommunityComponentDI {
    val module: Module = module {
        factory<CommunityNetworkDataSource> { CommunityNetworkDataSourceImpl(httpClient = get()) }
        factory<CommunityLocalDataSource> { CommunityLocalDataSourceImpl(preferenceManager = get()) }

        single<CommunityRepository> {
            CommunityRepositoryImpl(
                networkDataSource = get(),
                localDataSource = get(),
            )
        }

        factory<ObserveCommunityMembersUseCase> {
            val repository = get<CommunityRepository>()
            ObserveCommunityMembersUseCase { repository.members }
        }
        factory<LoadCommunityPageUseCase> { LoadCommunityPageUseCase(get<CommunityRepository>()::loadPage) }
        factory<ToggleMemberLikeUseCase> { ToggleMemberLikeUseCase(get<CommunityRepository>()::toggleLike) }
    }
}
