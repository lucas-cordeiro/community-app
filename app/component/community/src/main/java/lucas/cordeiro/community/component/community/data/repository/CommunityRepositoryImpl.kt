package lucas.cordeiro.community.component.community.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import lucas.cordeiro.community.component.community.data.mapper.toDomain
import lucas.cordeiro.community.component.community.data.network.CommunityNetworkDataSource
import lucas.cordeiro.community.component.community.data.storage.CommunityLocalDataSource
import lucas.cordeiro.community.component.community.domain.model.CommunityMember
import lucas.cordeiro.community.component.community.domain.repository.CommunityRepository

internal class CommunityRepositoryImpl(
    private val networkDataSource: CommunityNetworkDataSource,
    private val localDataSource: CommunityLocalDataSource,
) : CommunityRepository {

    private val rawMembers = MutableStateFlow<List<CommunityMember>>(emptyList())

    override val members: Flow<List<CommunityMember>> =
        combine(rawMembers, localDataSource.observeLikedIds()) { members, likedIds ->
            members.map { it.copy(isLiked = it.id in likedIds) }
        }

    override suspend fun loadPage(page: Int): Int {
        val fetched = networkDataSource.getCommunity(page).response.map { it.toDomain() }
        rawMembers.update { it + fetched }
        return fetched.size
    }

    override suspend fun toggleLike(id: Int) = localDataSource.toggleLike(id)
}
