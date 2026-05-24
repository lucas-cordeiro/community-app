package lucas.cordeiro.community.component.community.domain.usecase

import lucas.cordeiro.community.component.community.domain.model.CommunityMember

fun interface GetCommunityMembersUseCase {
    suspend operator fun invoke(page: Int): List<CommunityMember>
}
