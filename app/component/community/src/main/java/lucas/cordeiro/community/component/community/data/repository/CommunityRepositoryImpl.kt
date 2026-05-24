package lucas.cordeiro.community.component.community.data.repository

import kotlinx.coroutines.flow.Flow
import lucas.cordeiro.community.component.community.data.mapper.toDomain
import lucas.cordeiro.community.component.community.data.network.CommunityNetworkDataSource
import lucas.cordeiro.community.component.community.data.storage.CommunityLocalDataSource
import lucas.cordeiro.community.component.community.domain.model.CommunityMember
import lucas.cordeiro.community.component.community.domain.repository.CommunityRepository

internal class CommunityRepositoryImpl(
    private val networkDataSource: CommunityNetworkDataSource,
    private val localDataSource: CommunityLocalDataSource,
) : CommunityRepository {

    override suspend fun getCommunity(page: Int): List<CommunityMember> {
        val likedIds = localDataSource.getLikedIds()
        return networkDataSource.getCommunity(page).response.map { member ->
            member.toDomain(isLiked = member.id in likedIds)
        }
    }

    override fun observeLikedIds(): Flow<Set<Int>> = localDataSource.observeLikedIds()

    override suspend fun toggleLike(id: Int) = localDataSource.toggleLike(id)
}
