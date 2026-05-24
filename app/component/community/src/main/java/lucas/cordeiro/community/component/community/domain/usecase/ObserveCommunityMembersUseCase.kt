package lucas.cordeiro.community.component.community.domain.usecase

import kotlinx.coroutines.flow.Flow
import lucas.cordeiro.community.component.community.domain.model.CommunityMember

fun interface ObserveCommunityMembersUseCase {
    operator fun invoke(): Flow<List<CommunityMember>>
}
