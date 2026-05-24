package lucas.cordeiro.community.feature.community.stubs

import lucas.cordeiro.community.component.community.domain.model.CommunityMember

object CommunityMemberStub {

    fun create(
        id: Int = 1,
        firstName: String = "Name$id",
        topic: String = "topic",
        natives: List<String> = listOf("en"),
        learns: List<String> = listOf("pt"),
        referenceCnt: Int = id,
        isLiked: Boolean = false,
        nationality: String? = "gb",
    ): CommunityMember = CommunityMember(
        id = id,
        topic = topic,
        firstName = firstName,
        pictureUrl = "",
        natives = natives,
        learns = learns,
        referenceCnt = referenceCnt,
        isLiked = isLiked,
        nationality = nationality,
    )

    fun list(size: Int, startId: Int = 1): List<CommunityMember> =
        (startId until startId + size).map { create(id = it) }
}
