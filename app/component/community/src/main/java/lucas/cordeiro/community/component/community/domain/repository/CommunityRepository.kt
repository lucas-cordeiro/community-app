package lucas.cordeiro.community.component.community.domain.repository

import kotlinx.coroutines.flow.Flow
import lucas.cordeiro.community.component.community.domain.model.CommunityMember

interface CommunityRepository {
    val members: Flow<List<CommunityMember>>
    suspend fun loadPage(page: Int): Int
    suspend fun toggleLike(id: Int)
}
