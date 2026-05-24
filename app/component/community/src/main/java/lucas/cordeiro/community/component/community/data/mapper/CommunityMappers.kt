package lucas.cordeiro.community.component.community.data.mapper

import lucas.cordeiro.community.component.community.data.network.model.CommunityMemberResponse
import lucas.cordeiro.community.component.community.domain.model.CommunityMember

internal fun CommunityMemberResponse.toDomain(isLiked: Boolean): CommunityMember =
    CommunityMember(
        id = id,
        topic = topic,
        firstName = firstName,
        pictureUrl = pictureUrl,
        natives = natives,
        learns = learns,
        referenceCnt = referenceCnt,
        isLiked = isLiked,
    )
