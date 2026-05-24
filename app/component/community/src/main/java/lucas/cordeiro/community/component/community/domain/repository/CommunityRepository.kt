package lucas.cordeiro.community.component.community.domain.repository

import kotlinx.coroutines.flow.Flow
import lucas.cordeiro.community.component.community.domain.model.CommunityMember

interface CommunityRepository {
    suspend fun getCommunity(page: Int): List<CommunityMember>
    fun observeLikedIds(): Flow<Set<Int>>
    suspend fun toggleLike(id: Int)
}
